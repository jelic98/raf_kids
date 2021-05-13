package file;

import app.Config;
import data.Key;
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

    public void remove(Key key) {
        files.removeIf(data -> data.getKey().equals(key));
    }

    public FileData get(Key key) {
        for (FileData data : files) {
            if (data.getKey().equals(key)) {
                return data;
            }
        }

        return null;
    }

    public static String path(String directory, String file) {
        return directory.replace("{id}", String.valueOf(Config.LOCAL.getId())) + file;
    }
}
