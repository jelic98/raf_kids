package command;

import app.App;
import data.Key;
import file.FileData;

public class PullCommand implements Command {

    @Override
    public String getName() {
        return "pull";
    }

    @Override
    public void execute(String args) {
        String[] tokens = args.split(" ");
        String path = tokens[0];
        int version = -1;

        if (tokens.length == 2) {
            version = Integer.parseInt(tokens[1]);
        }

        FileData file = new FileData(path, version);
        Key key = new Key(file.hashCode());

        // TODO App.send(new PullAskMessage(key));
    }
}
