package command;

import app.ServentState;
import message.BroadcastMessage;

public class BroadcastCommand implements Command {

    @Override
    public String getName() {
        return "broadcast";
    }

    @Override
    public void execute(String args) {
        ServentState.broadcast(new BroadcastMessage(args));
    }
}
