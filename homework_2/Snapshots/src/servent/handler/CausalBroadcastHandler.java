package servent.handler;

import app.AppConfig;
import app.Servent;
import servent.message.*;
import servent.snapshot.BitcakeManager;
import servent.snapshot.CausalBroadcastShared;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class CausalBroadcastHandler implements Runnable {

    private static final Set<Message> receivedBroadcasts = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private final Message clientMessage;
    private final BitcakeManager bitcakeManager;

    public CausalBroadcastHandler(Message clientMessage, BitcakeManager bitcakeManager) {
        this.clientMessage = clientMessage;
        this.bitcakeManager = bitcakeManager;
    }

    @Override
    public void run() {
        if (clientMessage.getType() == MessageType.CAUSAL_BROADCAST ||
                clientMessage.getType() == MessageType.ASK) {

            Servent sender = clientMessage.getSender();
            Servent lastSender = clientMessage.getLastSender();
            String clock = ((CausalBroadcastMessage) clientMessage).getClock().toString();
            String content = clientMessage.getText() == null ? clientMessage.getType().toString() : clientMessage.getText();

            AppConfig.print(String.format("Got %s from %s broadcast by %s with clock %s", content, lastSender, sender, clock));

            if (AppConfig.IS_CLIQUE) {
                CausalBroadcastShared.addPendingMessage(clientMessage);
                CausalBroadcastShared.checkPendingMessages();
            } else {
                boolean absent = receivedBroadcasts.add(clientMessage);

                if (absent) {
                    CausalBroadcastShared.addPendingMessage(clientMessage);
                    CausalBroadcastShared.checkPendingMessages();

                    for (Servent neighbor : AppConfig.LOCAL_SERVENT.getNeighbors()) {
                        if (!clientMessage.containsSender(neighbor)) {
                            MessageUtil.sendMessage(clientMessage.setReceiver(neighbor).setSender());
                        }
                    }

                    if (clientMessage.getType() == MessageType.ASK) {
                        CausalBroadcastShared.setAskSender(lastSender);

                        int amount = bitcakeManager.getCurrentBitcakeAmount();

                        AppConfig.print(String.format("Sending TELL to %s (%d bitcakes)", lastSender, amount));

                        MessageUtil.sendMessage(new TellMessage(clientMessage.getReceiver(), lastSender, amount));
                    }
                }
            }
        }
    }
}
