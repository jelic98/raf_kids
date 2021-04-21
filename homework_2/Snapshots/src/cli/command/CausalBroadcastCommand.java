package cli.command;

import app.AppConfig;
import app.CausalBroadcastShared;
import app.ServentInfo;
import servent.message.CausalBroadcastMessage;
import servent.message.Message;
import servent.message.MessageUtil;

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

        for (Integer neighbor : AppConfig.myServentInfo.getNeighbors()) {
            ServentInfo neighborInfo = AppConfig.getInfoById(neighbor);

            /*
             * Causal Broadcast is implemented for clique only, so
             * we don't care about rebroadcast issues the way we do
             * in our regular Broadcast implementation.
             */
            Message broadcastMessage = new CausalBroadcastMessage(myInfo, neighborInfo, args, myClock);
            MessageUtil.sendMessage(broadcastMessage);
        }

        Message commitMessage = new CausalBroadcastMessage(myInfo, myInfo, args, myClock);
        CausalBroadcastShared.commitCausalMessage(commitMessage);
    }

}
