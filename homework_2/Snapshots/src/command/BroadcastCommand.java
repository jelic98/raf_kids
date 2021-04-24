package command;

import app.ServentState;
import app.App;
import app.Config;
import app.Servent;
import message.BroadcastMessage;

public class BroadcastCommand implements Command {

    @Override
    public String getName() {
        return "broadcast";
    }

    @Override
    public void execute(String args) {
        BroadcastMessage message = new BroadcastMessage(args);

        ServentState.commitMessage((BroadcastMessage) message.setReceiver(Config.LOCAL_SERVENT), true);

        for (Servent neighbor : Config.LOCAL_SERVENT.getNeighbors()) {
            App.send(message.setReceiver(neighbor));
        }
    }
}
