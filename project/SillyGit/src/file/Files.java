package file;

import app.App;
import app.Config;
import message.RedirectMessage;
import message.ReplicateMessage;
import servent.Servent;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Files {

    private final Set<FileData> files;
    private final Map<FileData, Servent> cache;
    private final Map<FileData, Set<Servent>> access;

    public Files() {
        files = Collections.newSetFromMap(new ConcurrentHashMap<>());
        cache = new ConcurrentHashMap<>();
        access = new ConcurrentHashMap<>();
    }

    public static String absolute(String directory, String file) {
        return directory.replace("{id}", String.valueOf(Config.LOCAL.getId())) + file;
    }

    public static String relative(String directory, String file) {
        return file.replace(directory.replace("{id}", String.valueOf(Config.LOCAL.getId())), "");
    }

    public FileData get(FileData data, Servent servent) {
        for (FileData f : files) {
            if (f.equals(data)) {
                if (servent != null) {
                    Set<Servent> access = this.access.get(f);
                    access.add(servent);

                    List<Servent> team = new ArrayList<>();

                    for (Servent s : access) {
                        if (s.getTeam().equals(servent.getTeam())) {
                            team.add(s);
                        }
                    }

                    if (team.size() > Config.TEAM_LIMIT) {
                        App.send(new ReplicateMessage(team.get(0), f, false));

                        for (int i = 1; i < team.size(); i++) {
                            App.send(new RedirectMessage(team.get(i), team.get(0), f));
                        }
                    }
                }

                return f;
            }
        }

        return null;
    }

    public FileData get(FileData data) {
        return get(data, null);
    }

    public void add(FileData data) {
        for (FileData existing : files) {
            if (existing.equals(data)) {
                data.transferHistory(existing);
                break;
            }
        }

        files.remove(data);
        files.add(data);

        if (!access.containsKey(data)) {
            access.put(data, Collections.newSetFromMap(new ConcurrentHashMap<>()));
        }
    }

    public void remove(FileData data) {
        files.removeIf(f -> f.equals(data));
    }

    public boolean contains(FileData data) {
        return files.contains(data);
    }

    public Servent getCached(FileData data) {
        if (cache.containsKey(data)) {
            Servent servent = cache.get(data);

            if (Config.NETWORK.containsServent(servent)) {
                return cache.get(data);
            } else {
                cache.remove(data);
                return Config.NETWORK.getServent(data.getKey());
            }
        } else {
            return Config.NETWORK.getServent(data.getKey());
        }
    }

    public void addCached(FileData data, Servent servent) {
        cache.put(data, servent);
    }

    public void replicate() {
        for (FileData f : files) {
            Servent[] servents = Config.NETWORK.getServents(f.getKey());

            if (f.isReplica() && servents[0].equals(Config.LOCAL)) {
                f.setReplica(false);
                App.print(String.format("Servent %s holds original file %s", Config.LOCAL, f));
            }

            for (Servent servent : servents) {
                if (!f.isReplica() && !servent.equals(Config.LOCAL)) {
                    App.send(new ReplicateMessage(servent, f, true));
                }
            }

            if (!f.isReplica() && !servents[0].equals(Config.LOCAL)) {
                f.setReplica(true);
                App.print(String.format("Servent %s holds replica file %s", Config.LOCAL, f));
            }
        }

        try {
            Thread.sleep(Config.REPLICATE_INTERVAL);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public String toString() {
        return files.toString();
    }
}
