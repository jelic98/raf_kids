package servent.handler;

import app.AppConfig;
import app.Servent;
import servent.message.*;
import servent.snapshot.BroadcastShared;
import servent.snapshot.SnapshotCollector;
import servent.snapshot.SnapshotManager;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class MessageHandler implements Runnable {

    private static final Set<Message> inbox = Collections.newSetFromMap(new ConcurrentHashMap<>());

    private final BroadcastMessage message;
    private final SnapshotCollector snapshotCollector;
    private final SnapshotManager snapshotManager;

    public MessageHandler(BroadcastMessage message, SnapshotCollector snapshotCollector) {
        this.message = message;
        this.snapshotCollector = snapshotCollector;

        snapshotManager = snapshotCollector.getSnapshotManager();
    }

    @Override
    public void run() {
        boolean absent = inbox.add(message);

        if (absent) {
            BroadcastShared.addPendingMessage(message);
            BroadcastShared.checkPendingMessages();

            for (Servent neighbor : AppConfig.LOCAL_SERVENT.getNeighbors()) {
                if (!message.containsSender(neighbor)) {
                    MessageUtil.sendMessage(message.setReceiver(neighbor).setSender());
                }
            }

            switch (message.getType()) {
                case ASK:
                    handleAsk();
                    break;
                case TELL:
                    handleTell();
                    break;
                case TRANSACTION:
                    handleTransaction();
                    break;
            }
        }
    }

    private void handleAsk() {
        AskMessage ask = (AskMessage) this.message;
        Servent lastSender = message.getLastSender();

        BroadcastShared.setAskSender(lastSender);

        AppConfig.print(String.format("Sending TELL to %s", lastSender));
        MessageUtil.sendMessage(new TellMessage(ask.getReceiver(), lastSender, snapshotManager.getSnapshot()));
    }

    private void handleTell() {
        TellMessage tell = (TellMessage) this.message;
        Servent askSender = BroadcastShared.getAskSender();

        if (askSender.equals(AppConfig.LOCAL_SERVENT)) {
            AppConfig.print(String.format("Received TELL from %s", tell.getSender()));
            snapshotCollector.addSnapshot(tell.getSender(), tell.getSnapshot());
        } else {
            AppConfig.print(String.format("Redirecting TELL from %s to %s", tell.getSender(), askSender));
            MessageUtil.sendMessage(tell.setReceiver(askSender).setSender());
        }
    }

    private void handleTransaction() {
        TransactionMessage transaction = (TransactionMessage) this.message;
        snapshotManager.plus(Integer.parseInt(transaction.getText()));
    }
}
