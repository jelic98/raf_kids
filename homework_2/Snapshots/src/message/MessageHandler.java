package message;

import app.*;
import snapshot.Snapshot;
import snapshot.SnapshotCollector;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class MessageHandler implements Runnable {

    private static final Set<Message> inbox = Collections.newSetFromMap(new ConcurrentHashMap<>());

    private final BroadcastMessage message;
    private final SnapshotCollector snapshotCollector;

    public MessageHandler(BroadcastMessage message, SnapshotCollector snapshotCollector) {
        this.message = message;
        this.snapshotCollector = snapshotCollector;
    }

    @Override
    public void run() {
        boolean absent = inbox.add(message);

        if (absent) {
            App.print(String.format("Received via %s: %s", message.getLastSender(), message));

            ServentState.addPendingMessage(message);
            ServentState.checkPendingMessages();

            switch (message.getType()) {
                case TRANSACTION:
                    handleTransaction();
                    break;
                case ASK:
                    handleAsk();
                    break;
                case TELL:
                    handleTell();
                    break;
            }

            for (Servent neighbor : Config.LOCAL_SERVENT.getNeighbors()) {
                if (!message.containsSender(neighbor)) {
                    App.print(String.format("Redirecting %s from %s to %s", message.getType(), message.getSender(), neighbor));
                    App.send(message.setReceiver(neighbor).setSender());
                }
            }

            if(message.getType() == Message.Type.STOP) {
                ServentSingle.stop();
            }
        }
    }


    private void handleTransaction() {
        TransactionMessage transaction = (TransactionMessage) this.message;

        if(transaction.getDestination().equals(Config.LOCAL_SERVENT)) {
            ServentState.getSnapshotManager().plus(transaction.getAmount(), transaction.getSender());
        }
    }

    private void handleAsk() {
        AskMessage ask = (AskMessage) this.message;
        Snapshot snapshot = ServentState.getSnapshotManager().getSnapshot();
        TellMessage tell = new TellMessage(Config.LOCAL_SERVENT, Config.LOCAL_SERVENT, snapshot, ask.getSender());

        ServentState.broadcast(tell);
    }

    private void handleTell() {
        TellMessage tell = (TellMessage) this.message;

        if(tell.getDestination().equals(Config.LOCAL_SERVENT)) {
            snapshotCollector.addSnapshot(tell.getSender(), tell.getSnapshot());
        }
    }
}
