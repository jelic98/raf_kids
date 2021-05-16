package command;

import app.App;
import app.Config;
import file.FileData;
import message.PullAskMessage;
import servent.Servent;

public class PullCommand implements Command {

    @Override
    public String getName() {
        return "pull";
    }

    @Override
    public void execute(String args) {
        String[] tokens = args.split(" ");
        String path = tokens[0];
        int version = tokens.length > 1 ? Integer.parseInt(tokens[1]) : FileData.VERSION_LATEST;

        FileData data = new FileData(path, version);
        Servent servent = Config.WORKSPACE.getCached(data);
        App.send(new PullAskMessage(servent, data));
    }
}
