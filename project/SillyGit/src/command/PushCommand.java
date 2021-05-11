package command;

import app.Config;

public class PushCommand implements Command {

    @Override
    public String getName() {
        return "push";
    }

    @Override
    public void execute(String args) {
        String[] tokens = args.split(" ");

        Config.CHORD.putValue(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]));
    }
}
