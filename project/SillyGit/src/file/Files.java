package file;

import app.Config;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Files {

    private Set<FileData> files;

    public Files() {
        files = Collections.newSetFromMap(new ConcurrentHashMap<>());
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

    @Override
    public String toString() {
        return files.toString();
    }

    public static String absolute(String directory, String file) {
        return directory.replace("{id}", String.valueOf(Config.LOCAL.getId())) + file;
    }

    public static String relative(String directory, String file) {
        return file.replace(directory.replace("{id}", String.valueOf(Config.LOCAL.getId())), "");
    }
}
