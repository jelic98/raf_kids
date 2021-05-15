package command;

import app.App;
import app.Config;
import file.FileData;
import file.FileHandler;
import file.Files;
import message.PullAskMessage;
import message.PushAskMessage;
import servent.Servent;

import java.io.File;

public class PushCommand implements Command {

    @Override
    public String getName() {
        return "push";
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
                FileData data = new FileData(path);
                data.load(Config.WORKSPACE_PATH);

                FileData existing = Config.WORKSPACE.get(data);

                if (existing != null) {
                    if (existing.getContent().equals(data.getContent())) {
                        App.print(String.format("File %s not changed", data));
                        return;
                    }

                    data.setVersion(existing.getVersion() + 1);
                }

                Servent servent = Config.WORKSPACE.getCached(data);
                App.send(new PushAskMessage(servent, data));
            }
        });
    }
}
