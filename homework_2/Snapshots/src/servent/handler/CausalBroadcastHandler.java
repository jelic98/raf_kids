package servent.handler;

import app.AppConfig;
import app.CausalBroadcastShared;
import app.ServentInfo;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.util.MessageUtil;
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
public class CausalBroadcastHandler implements MessageHandler {

    private static final Set<Message> receivedBroadcasts = Collections.newSetFromMap(new ConcurrentHashMap<>());
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

            String text = String.format("Got %s from %s broadcast by %s",
                    clientMessage.getMessageText(), lastSenderInfo, senderInfo);

            AppConfig.timestampedStandardPrint(text);

            if (AppConfig.IS_CLIQUE) {
                CausalBroadcastShared.addPendingMessage(clientMessage);
                CausalBroadcastShared.checkPendingMessages();
            } else {
                //Try to put in the set. Thread safe add ftw.
                boolean didPut = receivedBroadcasts.add(clientMessage);

                if (didPut) {
                    //New message for us. Rebroadcast it.
                    CausalBroadcastShared.addPendingMessage(clientMessage);
                    CausalBroadcastShared.checkPendingMessages();

                    AppConfig.timestampedStandardPrint("Rebroadcasting... " + receivedBroadcasts.size());

                    for (Integer neighbor : AppConfig.myServentInfo.getNeighbors()) {
                        //Same message, different receiver, and add us to the route table.
                        MessageUtil.sendMessage(clientMessage.changeReceiver(neighbor).makeMeASender());
                    }
                } else {
                    //We already got this from somewhere else. /ignore
                    AppConfig.timestampedStandardPrint("Already had this. No rebroadcast.");
                }
            }
        }
    }

}
