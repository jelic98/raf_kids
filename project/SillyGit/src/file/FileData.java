package file;

import app.App;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class FileData {

    private String path;
    private String content;
    private int version;

    public FileData(String path, int version) {
        this.path = path;
        this.version = version;

        load();
    }

    public FileData(String path) {
        this(path, 0);
    }

    public void load() {
        try {
            content = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.US_ASCII);
        } catch (IOException e) {
            App.error(String.format("Cannot open file on path %s", path));
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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FileData) {
            FileData f = (FileData) obj;
            return getPath().equals(f.getPath()) && getVersion() == f.getVersion();
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPath(), getVersion());
    }

    @Override
    public String toString() {
        return getPath() + "[" + getVersion() + "]";
    }
}
