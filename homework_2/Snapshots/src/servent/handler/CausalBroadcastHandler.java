package servent.handler;

import app.AppConfig;
import app.ServentInfo;
import servent.message.*;
import servent.snapshot.BitcakeManager;
import servent.snapshot.CausalBroadcastShared;

import java.util.ArrayList;
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
    private final BitcakeManager bitcakeManager;

    public CausalBroadcastHandler(Message clientMessage, BitcakeManager bitcakeManager) {
        this.clientMessage = clientMessage;
        this.bitcakeManager = bitcakeManager;
    }

    @Override
    public void run() {
        if (clientMessage.getMessageType() == MessageType.CAUSAL_BROADCAST ||
                clientMessage.getMessageType() == MessageType.ASK) {
            ServentInfo senderInfo = clientMessage.getOriginalSenderInfo();
            ServentInfo lastSenderInfo = clientMessage.getRoute().size() == 0 ?
                    clientMessage.getOriginalSenderInfo() :
                    clientMessage.getRoute().get(clientMessage.getRoute().size() - 1);

            String text = String.format("Got %s from %s broadcast by %s with clock %s",
                    clientMessage.getMessageText() == null ? clientMessage.getMessageType() : clientMessage.getMessageText(),
                    lastSenderInfo, senderInfo, ((CausalBroadcastMessage) clientMessage).getSenderVectorClock());

            AppConfig.timestampedStandardPrint(text);

            if (AppConfig.IS_CLIQUE) {
                CausalBroadcastShared.addPendingMessage(clientMessage);
                CausalBroadcastShared.checkPendingMessages();
            } else {
                boolean didPut = receivedBroadcasts.add(clientMessage);

                if (didPut && clientMessage.getOriginalSenderInfo().getId() != AppConfig.myServentInfo.getId()) {
                    CausalBroadcastShared.addPendingMessage(clientMessage);
                    CausalBroadcastShared.checkPendingMessages();

                    for (Integer neighbor : AppConfig.myServentInfo.getNeighbors()) {
                        if(!clientMessage.routeContains(neighbor)) {
                            MessageUtil.sendMessage(clientMessage.changeReceiver(neighbor).makeMeASender());
                        }
                    }

                    if (clientMessage.getMessageType() == MessageType.ASK) {
                        CausalBroadcastShared.setAskSender(clientMessage.getLastSenderInfo().getId());

                        int amount = bitcakeManager.getCurrentBitcakeAmount();

                        AppConfig.timestampedStandardPrint(String.format("Sending TELL message (%d bitcakes) to %d",
                                amount, clientMessage.getLastSenderInfo().getId()));

                        MessageUtil.sendMessage(new TellMessage(clientMessage.getReceiverInfo(),
                                clientMessage.getLastSenderInfo(), amount));
                    }
                }
            }
        }
    }
}
