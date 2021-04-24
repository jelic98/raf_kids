package cli.command;

import app.AppConfig;
import app.Servent;
import servent.message.CausalBroadcastMessage;
import servent.message.MessageUtil;
import servent.snapshot.CausalBroadcastShared;

public class CausalBroadcastCommand implements Command {

    @Override
    public String getName() {
        return "causal_broadcast";
    }

    @Override
    public void execute(String args) {
        CausalBroadcastMessage message = new CausalBroadcastMessage(args);

        for (Servent neighbor : AppConfig.LOCAL_SERVENT.getNeighbors()) {
            MessageUtil.sendMessage(message.setReceiver(neighbor));
        }

        CausalBroadcastShared.commitMessage((CausalBroadcastMessage) message.setReceiver(AppConfig.LOCAL_SERVENT), true);
    }
}
