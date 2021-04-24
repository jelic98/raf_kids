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

    private static final Set<Message> inbox = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private final Message message;
    private final BitcakeManager bitcakeManager;

    public CausalBroadcastHandler(Message message, BitcakeManager bitcakeManager) {
        this.message = message;
        this.bitcakeManager = bitcakeManager;
    }

    @Override
    public void run() {
        if (message.getType() == MessageType.CAUSAL_BROADCAST ||
                message.getType() == MessageType.ASK) {

            Servent sender = message.getSender();
            Servent lastSender = message.getLastSender();
            String clock = ((CausalBroadcastMessage) message).getClock().toString();
            String content = message.getText() == null ? message.getType().toString() : message.getText();

            AppConfig.print(String.format("Got %s from %s broadcast by %s with clock %s", content, lastSender, sender, clock));

            boolean absent = inbox.add(message);

            if (absent) {
                CausalBroadcastShared.addPendingMessage(message);
                CausalBroadcastShared.checkPendingMessages();

                for (Servent neighbor : AppConfig.LOCAL_SERVENT.getNeighbors()) {
                    if (!message.containsSender(neighbor)) {
                        MessageUtil.sendMessage(message.setReceiver(neighbor).setSender());
                    }
                }

                if (message.getType() == MessageType.ASK) {
                    CausalBroadcastShared.setAskSender(lastSender);

                    int amount = bitcakeManager.getCurrentBitcakeAmount();

                    AppConfig.print(String.format("Sending TELL to %s (%d bitcakes)", lastSender, amount));

                    MessageUtil.sendMessage(new TellMessage(message.getReceiver(), lastSender, amount));
                }
            }
        }
    }
}
