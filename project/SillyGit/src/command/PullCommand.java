package command;

import app.App;
import app.Config;
import file.FileData;
import file.FileHandler;
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
        File path = new File(tokens[0]);
        int version = tokens.length > 1 ? Integer.parseInt(tokens[1]) : -1;

        new FileHandler().forEach(path, new FileHandler.Handler<String>() {
            @Override
            public void handle(String path) {
                FileData data = new FileData(path, version);
                Servent[] servents = Config.SYSTEM.getServents(data.getKey());

                for (Servent servent : servents) {
                    App.send(new PullAskMessage(servent, data.getKey()));
                }
            }
        });
    }
}
