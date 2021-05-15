package file;

import app.App;
import app.Config;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Objects;

public class FileData implements Serializable {

    private final String path;
    private String content;
    private int version;

    public FileData(String path, int version) {
        this.path = Files.relative(Config.WORKSPACE_PATH, path);
        this.version = version;
    }

    public FileData(String path) {
        this(path, -1);
    }

    public void load(String location) {
        try {
            content = new String(java.nio.file.Files.readAllBytes(Paths.get(Files.absolute(location, path))), StandardCharsets.US_ASCII);
        } catch (IOException e) {
            App.error(String.format("Cannot load file %s (%s)", path, e.getMessage()));
        }
    }

    public void save(String location) {
        File file = new File(Files.absolute(location, path));

        File directory = new File(file.getParentFile().getPath());
        directory.mkdirs();

        try (PrintWriter out = new PrintWriter(file)) {
            out.write(content);
        } catch (IOException e) {
            App.error(String.format("Cannot save file %s (%s)", path, e.getMessage()));
        }
    }

    public String getPath() {
        return path;
    }

    public String getContent() {
        return content;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
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
        return getPath() + "[" + getVersion() + "]";
    }
}
