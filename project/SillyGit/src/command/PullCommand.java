package command;

import app.App;
import app.Config;

public class PullCommand implements Command {

    @Override
    public String getName() {
        return "pull";
    }

    @Override
    public void execute(String args) {
        int key = Integer.parseInt(args);
        int value = Config.CHORD.getValue(key);

        if (value == -2) {
            App.print("Pulling");
        } else if (value == -1) {
            App.error("Unknown key: " + key);
        } else {
            App.print(key + ": " + value);
        }
    }
}
