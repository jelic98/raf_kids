package command;

import app.App;
import app.Config;
import app.Servent;
import javafx.scene.paint.Stop;
import message.Message;
import message.MessageHandler;
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
