package command;

import app.*;
import data.Key;
import data.Value;

public class PullCommand implements Command {

    @Override
    public String getName() {
        return "pull";
    }

    @Override
    public void execute(String args) {
        Key key = new Key(Integer.parseInt(args));
        Value value = Config.CHORD.getValue(key);

        if (value == null) {
            App.print("Pulling key: " + key);
        } else if (value.get() == -1) {
            App.print("Unknown key: " + key);
        } else {
            App.print("Pulled: " + key + "-> " + value);
        }
    }
}
