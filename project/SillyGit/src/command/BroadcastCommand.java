package command;

import message.BroadcastMessage;
import message.MessageHandler;

public class BroadcastCommand implements Command {

    @Override
    public String getName() {
        return "broadcast";
    }

    @Override
    public void execute(String args) {
        MessageHandler.handle(new BroadcastMessage(args));
    }
}
