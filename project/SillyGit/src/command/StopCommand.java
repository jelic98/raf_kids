package command;

import app.ServentSingle;

public class StopCommand implements Command {

    @Override
    public String getName() {
        return "stop";
    }

    @Override
    public void execute(String args) {
        ServentSingle.stop();
    }
}
