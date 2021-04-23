package cli.command;

import app.AppConfig;
import servent.snapshot.CausalBroadcastShared;
import app.ServentInfo;
import servent.message.CausalBroadcastMessage;
import servent.message.Message;
import servent.message.MessageUtil;
import java.util.Map;

public class CausalBroadcastCommand implements Command {

    @Override
    public String getName() {
        return "causal_broadcast";
    }

    @Override
    public void execute(String args) {
        if (args == null) {
            AppConfig.timestampedErrorPrint("No message to causally broadcast");
            return;
        }

        ServentInfo myInfo = AppConfig.myServentInfo;
        Map<Integer, Integer> myClock = CausalBroadcastShared.getVectorClock();

        Message broadcastMessage = new CausalBroadcastMessage(myInfo, null, args, myClock);

        for (Integer neighbor : AppConfig.myServentInfo.getNeighbors()) {
            MessageUtil.sendMessage(broadcastMessage.changeReceiver(neighbor));
        }

        CausalBroadcastShared.commitCausalMessage(broadcastMessage.changeReceiver(myInfo.getId()), true);
    }
}
