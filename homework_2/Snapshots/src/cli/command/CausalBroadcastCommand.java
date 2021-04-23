package cli.command;

import app.AppConfig;
import app.CausalBroadcastShared;
import app.ServentInfo;
import servent.message.CausalBroadcastMessage;
import servent.message.Message;
import servent.message.util.MessageUtil;

import java.util.Map;

public class CausalBroadcastCommand implements CLICommand {

    @Override
    public String commandName() {
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
    }
}
