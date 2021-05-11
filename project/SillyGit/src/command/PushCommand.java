package command;

import app.Config;
import data.Key;
import data.Value;

public class PushCommand implements Command {

    @Override
    public String getName() {
        return "push";
    }

    @Override
    public void execute(String args) {
        String[] tokens = args.split(" ");

        Key key = new Key(Integer.parseInt(tokens[0]));
        Value value = new Value(Integer.parseInt(tokens[1]));

        Config.CHORD.putValue(key, value);
    }
}
