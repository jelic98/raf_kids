package command;

import app.App;
import app.Config;
import file.FileData;
import file.FileHandler;
import message.RemoveMessage;
import servent.Servent;

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

        new FileHandler().forEach(path, new FileHandler.Handler<String>() {
            @Override
            public void handle(String path) {
                FileData data = new FileData(path);

                Config.WORKSPACE.remove(data.getKey());

                Servent[] servents = Config.SYSTEM.getServents(data.getKey());

                for (Servent servent : servents) {
                    App.send(new RemoveMessage(servent, data.getKey()));
                }
            }
        });
    }
}
