package file;

import app.App;
import app.Config;
import message.ReplicateMessage;
import servent.Servent;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Files {

    private Set<FileData> files;

    public Files() {
        files = Collections.newSetFromMap(new ConcurrentHashMap<>());
    }

    public static String absolute(String directory, String file) {
        return directory.replace("{id}", String.valueOf(Config.LOCAL.getId())) + file;
    }

    public static String relative(String directory, String file) {
        return file.replace(directory.replace("{id}", String.valueOf(Config.LOCAL.getId())), "");
    }

    public void add(FileData data) {
        files.remove(data);
        files.add(data);
    }

    public void remove(FileData data) {
        files.removeIf(f -> f.equals(data));
    }

    public FileData get(FileData data) {
        for (FileData f : files) {
            if (f.equals(data)) {
                return f;
            }
        }

        return null;
    }

    public boolean contains(FileData data) {
        return files.contains(data);
    }

    public void replicate() {
        for (FileData f : files) {
            Servent[] servents = Config.SYSTEM.getServents(f.getKey());

            if (servents[0].equals(Config.LOCAL)) {
                for (int i = 1; i < servents.length; i++) {
                    App.send(new ReplicateMessage(servents[i], f));
                }
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
