package command;

import app.Config;
import message.Message;
import message.MessageHandler;

public class BroadcastCommand implements Command {

    @Override
    public String getName() {
        return "broadcast";
    }

    @Override
    public void execute(String args) {
        MessageHandler.handle(new Message(Message.Type.BROADCAST, args, Config.LOCAL_SERVENT, Config.LOCAL_SERVENT));
    }
}
