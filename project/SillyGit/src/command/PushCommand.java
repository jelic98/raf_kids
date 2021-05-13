package command;

import app.App;
import app.Config;
import data.Key;
import file.FileData;
import file.FileHandler;
import file.Files;
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
        File path = new File(Files.path(Config.WORKSPACE_PATH, args));

        new FileHandler().forEach(path, new FileHandler.Handler<String>() {
            @Override
            public void handle(String path) {
                FileData data = new FileData(path);
                data.load(Config.WORKSPACE_PATH);

                FileData existing = Config.WORKSPACE.get(data.getKey());

                if (existing != null) {
                    if (existing.getContent().equals(data.getContent())) {
                        App.print(String.format("File on path %s is not changed", path));
                        return;
                    }

                    data.setVersion(existing.getVersion() + 1);
                }

                Servent[] servents = Config.SYSTEM.getServents(data.getKey());

                for (Servent servent : servents) {
                    App.send(new PushAskMessage(servent, data));
                }
            }
        });
    }
}
