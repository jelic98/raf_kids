package servent.handler;

import app.AppConfig;
import app.CausalBroadcastShared;
import app.ServentInfo;
import servent.message.CausalBroadcastMessage;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.MessageUtil;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Handles the CAUSAL_BROADCAST message. Fairly simple, as we assume that we are
 * in a clique. We add the message to a pending queue, and let the check on the queue
 * take care of the rest.
 *
 * @author bmilojkovic
 */
public class CausalBroadcastHandler implements Runnable {

    private static final Set<Message> receivedBroadcasts = Collections.newSetFromMap(new ConcurrentHashMap<Message, Boolean>());
    private final Message clientMessage;

    public CausalBroadcastHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

    @Override
    public void run() {
        if (clientMessage.getMessageType() == MessageType.CAUSAL_BROADCAST) {
            ServentInfo senderInfo = clientMessage.getOriginalSenderInfo();
            ServentInfo lastSenderInfo = clientMessage.getRoute().size() == 0 ?
                    clientMessage.getOriginalSenderInfo() :
                    clientMessage.getRoute().get(clientMessage.getRoute().size() - 1);

            String text = String.format("Got %s from %s broadcast by %s with clock %s",
                    clientMessage.getMessageText(), lastSenderInfo, senderInfo,
                    ((CausalBroadcastMessage) clientMessage).getSenderVectorClock());

            AppConfig.timestampedStandardPrint(text);

            if (AppConfig.IS_CLIQUE) {
                CausalBroadcastShared.addPendingMessage(clientMessage);
                CausalBroadcastShared.checkPendingMessages();
            } else {
                boolean didPut = receivedBroadcasts.add(clientMessage);

                if (didPut && clientMessage.getOriginalSenderInfo().getId() != AppConfig.myServentInfo.getId()) {
                    CausalBroadcastShared.addPendingMessage(clientMessage);
                    CausalBroadcastShared.checkPendingMessages();

                    AppConfig.timestampedStandardPrint("Rebroadcasting... " + receivedBroadcasts.size());

                    for (Integer neighbor : AppConfig.myServentInfo.getNeighbors()) {
                        MessageUtil.sendMessage(clientMessage.changeReceiver(neighbor).makeMeASender());
                    }
                } else {
                    AppConfig.timestampedStandardPrint("Already had this. No rebroadcast.");
                }
            }
        }
    }
}
