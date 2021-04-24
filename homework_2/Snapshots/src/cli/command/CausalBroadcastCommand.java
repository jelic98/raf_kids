package cli.command;

import app.AppConfig;
import app.Servent;
import servent.message.CausalBroadcastMessage;
import servent.message.Message;
import servent.message.MessageUtil;
import servent.snapshot.CausalBroadcastShared;

public class CausalBroadcastCommand implements Command {

    @Override
    public String getName() {
        return "causal_broadcast";
    }

    @Override
    public void execute(String args) {
        Message broadcastMessage = new CausalBroadcastMessage(AppConfig.LOCAL_SERVENT,
                null, args, CausalBroadcastShared.getVectorClock());

        for (Servent neighbor : AppConfig.LOCAL_SERVENT.getNeighbors()) {
            MessageUtil.sendMessage(broadcastMessage.setReceiver(neighbor));
        }

        CausalBroadcastShared.commitMessage(broadcastMessage.setReceiver(AppConfig.LOCAL_SERVENT), true);
    }
}
