package command;

import app.App;
import app.Config;
import file.FileData;
import file.FileHandler;
import file.Files;
import message.PullAskMessage;
import servent.Servent;

import java.io.File;

public class PullCommand implements Command {

    @Override
    public String getName() {
        return "pull";
    }

    @Override
    public void execute(String args) {
        String[] tokens = args.split(" ");
        File path = new File(Files.absolute(Config.WORKSPACE_PATH, tokens[0]));
        int version = tokens.length > 1 ? Integer.parseInt(tokens[1]) : -1;

        if (path.exists()) {
            new FileHandler().forEach(path, new FileHandler.Handler<String>() {
                @Override
                public void handle(String path) {
                    pull(new FileData(path, version));
                }
            });
        } else {
            pull(new FileData(args, version));
        }
    }

    private void pull(FileData data) {
        Servent servent = Config.WORKSPACE.getCached(data);
        App.send(new PullAskMessage(servent, data));
    }
}
