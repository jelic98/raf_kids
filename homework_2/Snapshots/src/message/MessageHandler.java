package message;

import app.App;
import app.Config;
import app.Servent;
import snapshot.SnapshotCollector;
import snapshot.SnapshotManager;
import app.ServentState;

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
            ServentState.addPendingMessage(message);
            ServentState.checkPendingMessages();

            for (Servent neighbor : Config.LOCAL_SERVENT.getNeighbors()) {
                if (!message.containsSender(neighbor)) {
                    App.send(message.setReceiver(neighbor).setSender());
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

        ServentState.setAskSender(lastSender);

        App.print(String.format("Sending TELL to %s", lastSender));
        App.send(new TellMessage(ask.getReceiver(), lastSender, snapshotManager.getSnapshot()));
    }

    private void handleTell() {
        TellMessage tell = (TellMessage) this.message;
        Servent askSender = ServentState.getAskSender();

        if (askSender.equals(Config.LOCAL_SERVENT)) {
            App.print(String.format("Received TELL from %s", tell.getSender()));
            snapshotCollector.addSnapshot(tell.getSender(), tell.getSnapshot());
        } else {
            App.print(String.format("Redirecting TELL from %s to %s", tell.getSender(), askSender));
            App.send(tell.setReceiver(askSender).setSender());
        }
    }

    private void handleTransaction() {
        TransactionMessage transaction = (TransactionMessage) this.message;
        snapshotManager.plus(Integer.parseInt(transaction.getText()));
    }
}
