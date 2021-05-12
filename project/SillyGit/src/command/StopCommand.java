package command;

import app.App;
import message.StopMessage;

public class StopCommand implements Command {

    @Override
    public String getName() {
        return "stop";
    }

    @Override
    public void execute(String args) {
        App.send(new StopMessage());
    }
}
