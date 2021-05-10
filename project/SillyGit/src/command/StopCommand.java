package command;

import message.MessageHandler;
import message.StopMessage;

public class StopCommand implements Command {

    @Override
    public String getName() {
        return "stop";
    }

    @Override
    public void execute(String args) {
        MessageHandler.handle(new StopMessage());
    }
}
