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
            App.print(String.format("Received %s from %s via %s", message.getType(), message.getSender(), message.getLastSender()));

            ServentState.addPendingMessage(message);
            ServentState.checkPendingMessages();

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

            for (Servent neighbor : Config.LOCAL_SERVENT.getNeighbors()) {
                if (!message.containsSender(neighbor)) {
                    App.print(String.format("Redirecting %s from %s to %s", message.getType(), message.getSender(), neighbor));
                    App.send(message.setReceiver(neighbor).setSender());
                }
            }
        }
    }

    private void handleAsk() {
        AskMessage ask = (AskMessage) this.message;

        Servent lastSender = ask.getLastSender();

        App.print(String.format("Sending TELL to %s", lastSender));

        TellMessage tell = new TellMessage(Config.LOCAL_SERVENT, Config.LOCAL_SERVENT, snapshotManager.getSnapshot());

        ServentState.commitMessage(tell, true);

        App.send(tell.setReceiver(lastSender));
    }

    private void handleTell() {
        TellMessage tell = (TellMessage) this.message;

        snapshotCollector.addSnapshot(tell.getSender(), tell.getSnapshot());
    }

    private void handleTransaction() {
        TransactionMessage transaction = (TransactionMessage) this.message;

        snapshotManager.plus(Integer.parseInt(transaction.getText()));
    }
}
