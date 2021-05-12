package command;

import app.App;
import app.Config;
import data.Data;
import data.Key;
import data.Value;
import message.MessageHandler;
import message.PullAskMessage;
import message.PullTellMessage;

public class PullCommand implements Command {

    @Override
    public String getName() {
        return "pull";
    }

    @Override
    public void execute(String args) {
        Key key = new Key(Integer.parseInt(args));
        Value value = Config.SYSTEM.getValue(key);

        if (value == null) {
            App.send(new PullAskMessage(key));
        } else {
            MessageHandler.handle(new PullTellMessage(null, new Data(key, value)));
        }
    }
}
