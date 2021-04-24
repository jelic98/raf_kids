package cli.command;

import app.AppConfig;
import app.Servent;
import servent.message.BroadcastMessage;
import servent.message.MessageUtil;
import servent.snapshot.BroadcastShared;

public class BroadcastCommand implements Command {

    @Override
    public String getName() {
        return "broadcast";
    }

    @Override
    public void execute(String args) {
        BroadcastMessage message = new BroadcastMessage(args);

        for (Servent neighbor : AppConfig.LOCAL_SERVENT.getNeighbors()) {
            MessageUtil.sendMessage(message.setReceiver(neighbor));
        }

        BroadcastShared.commitMessage((BroadcastMessage) message.setReceiver(AppConfig.LOCAL_SERVENT), true);
    }
}
