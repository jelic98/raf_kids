package command;

import app.App;
import app.Config;
import file.FileData;
import file.FileHandler;
import file.Files;
import message.AddMessage;
import servent.Servent;

import java.io.File;

public class AddCommand implements Command {

    @Override
    public String getName() {
        return "add";
    }

    @Override
    public void execute(String args) {
        File path = new File(Files.absolute(Config.WORKSPACE_PATH, args));

        if (!path.exists()) {
            App.error(String.format("Cannot open file or directory on path %s", args));
            return;
        }

        new FileHandler().forEach(path, new FileHandler.Handler<String>() {
            @Override
            public void handle(String path) {
                // TODO Handle newest file version (-1)
                FileData data = new FileData(path);
                data.load(Config.WORKSPACE_PATH);

                Config.WORKSPACE.add(data);

                Servent servent = Config.NETWORK.getServent(data.getKey());
                App.send(new AddMessage(servent, data));
            }
        });
    }
}
