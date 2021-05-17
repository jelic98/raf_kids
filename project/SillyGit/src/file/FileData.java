package file;

import app.App;
import app.Config;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class FileData implements Serializable {

    public static final int VERSION_LATEST = -1;

    private final String path;
    private final Map<Integer, String> history;
    private String content;
    private int version;
    private boolean replica;

    public FileData(String path, int version) {
        this.path = Files.relative(Config.WORKSPACE_PATH, path);
        this.version = version;

        history = new ConcurrentHashMap<>();
    }

    public FileData(String path) {
        this(path, VERSION_LATEST);
    }

    public FileData(FileData data) {
        this(data.getPath(), data.getVersion());

        setContent(data.getContent());
        setReplica(true);
        transferHistory(data);
    }

    public void load(String location) {
        try {
            setContent(new String(java.nio.file.Files.readAllBytes(Paths.get(Files.absolute(location, getPath()))), StandardCharsets.US_ASCII).trim());
        } catch (IOException e) {
            App.error(String.format("Cannot load file %s (%s)", getPath(), e.getMessage()));
        }
    }

    public boolean load(int version) {
        if (version == VERSION_LATEST) {
            if (history.isEmpty()) {
                return true;
            } else {
                int[] versions = history.keySet().stream().mapToInt(i -> i).toArray();
                Arrays.sort(versions);
                version = versions[versions.length - 1];
            }
        }

        if (history.containsKey(version)) {
            setContent(history.get(version));
            setVersion(version);
            return true;
        }

        return false;
    }

    public void save(String location) {
        File file = new File(Files.absolute(location, getPath()));

        File directory = new File(file.getParentFile().getPath());
        directory.mkdirs();

        try (PrintWriter out = new PrintWriter(file)) {
            out.write(getContent());
        } catch (IOException e) {
            App.error(String.format("Cannot save file %s (%s)", getPath(), e.getMessage()));
        }

        history.put(getVersion(), getContent());
    }

    public void transferHistory(FileData data) {
        history.putAll(data.history);

        if (getContent() != null) {
            history.put(getVersion(), getContent());
        }
    }

    public String getPath() {
        return path;
    }

    public String getContent() {
        return content;
    }

    private void setContent(String content) {
        this.content = content;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public boolean isReplica() {
        return replica;
    }

    public void setReplica(boolean replica) {
        this.replica = replica;
    }

    public Key getKey() {
        return new Key(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FileData) {
            FileData f = (FileData) obj;
            return getPath().equals(f.getPath());
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPath());
    }

    @Override
    public String toString() {
        return getPath() + "[" + getVersion() + "] \"" + getContent() + "\"";
    }
}
