package command;

import app.App;
import app.ServentSingle;
import app.ServentState;
import message.StopMessage;

public class StopCommand implements Command {

    @Override
    public String getName() {
        return "stop";
    }

    @Override
    public void execute(String args) {
        ServentSingle.stop();

        ServentState.broadcast(new StopMessage());
    }
}
