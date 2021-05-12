package file;

import app.App;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileData {

    private String path;
    private String content;
    private int version;

    public FileData(String path) {
        this.path = path;

        load();
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
}
