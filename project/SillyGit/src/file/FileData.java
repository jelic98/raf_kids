package file;

import app.App;
import app.Config;
import data.Key;
import data.Value;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
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
    }

    public FileData(String path) {
        this(path, 0);
    }

    public void load(String location) {
        try {
            content = new String(Files.readAllBytes(Paths.get(location + path)), StandardCharsets.US_ASCII);
        } catch (IOException e) {
            App.error(String.format("Cannot open file on path %s", path));
        }
    }

    public void save(String location) {
        File file = new File(location + path);

        File directory = new File(file.getParentFile().getPath());
        directory.mkdirs();

        try (PrintWriter out = new PrintWriter(file)) {
            out.println(content);
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

    public void setVersion(int version) {
        this.version = version;
    }

    public Key getKey() {
        return new Key(hashCode());
    }

    public Value getValue() {
        return new Value(this);
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
