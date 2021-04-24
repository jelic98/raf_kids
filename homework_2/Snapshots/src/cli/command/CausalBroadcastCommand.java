package cli.command;

import app.AppConfig;
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
        Message broadcastMessage = new CausalBroadcastMessage(AppConfig.myServentInfo,
                null, args, CausalBroadcastShared.getVectorClock());

        for (Integer neighbor : AppConfig.myServentInfo.getNeighbors()) {
            MessageUtil.sendMessage(broadcastMessage.changeReceiver(neighbor));
        }

        CausalBroadcastShared.commitCausalMessage(broadcastMessage.changeReceiver(AppConfig.myServentInfo.getId()), true);
    }
}
