package servent.handler;

import app.AppConfig;
import app.Servent;
import servent.message.*;
import servent.snapshot.CausalBroadcastShared;
import servent.snapshot.SnapshotManager;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class CausalBroadcastHandler implements Runnable {

    private static final Set<Message> inbox = Collections.newSetFromMap(new ConcurrentHashMap<>());

    private final CausalBroadcastMessage message;
    private final SnapshotManager snapshotManager;

    public CausalBroadcastHandler(CausalBroadcastMessage message, SnapshotManager snapshotManager) {
        this.message = message;
        this.snapshotManager = snapshotManager;
    }

    @Override
    public void run() {
        Servent sender = message.getSender();
        Servent lastSender = message.getLastSender();
        String clock = message.getClock().toString();
        String content = message.getText() == null ? message.getType().toString() : message.getText();

        AppConfig.print(String.format("Broadcast %s from %s via %s with clock %s", content, sender, lastSender, clock));

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

                AppConfig.print(String.format("Sending TELL to %s", lastSender));

                MessageUtil.sendMessage(new TellMessage(message.getReceiver(), lastSender, snapshotManager.getSnapshot()));
            }
        }
    }
}
