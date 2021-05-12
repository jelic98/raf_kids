package command;

import app.App;
import app.Config;
import file.FileManager;

import java.io.File;

public class RemoveCommand implements Command {

    @Override
    public String getName() {
        return "remove";
    }

    @Override
    public void execute(String args) {
        File path = new File(args);

        if (!path.exists()) {
            App.error(String.format("Cannot open file or directory on path %s", path));
            return;
        }

        Config.FILES.forEach(path, new FileManager.Handler<String>() {
            @Override
            public void handle(String path) {
                Config.FILES.remove(path);
            }
        });
    }
}
