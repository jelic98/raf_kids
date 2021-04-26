package message;

import app.*;
import snapshot.Snapshot;
import snapshot.SnapshotCollector;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class MessageHandler implements Runnable {

    private static final Set<Message> inbox = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private static Set<TransactionMessage> oldMessages = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private static AskMessage token;

    private final BroadcastMessage message;
    private final SnapshotCollector collector;

    public MessageHandler(BroadcastMessage message, SnapshotCollector collector) {
        this.message = message;
        this.collector = collector;
    }

    @Override
    public void run() {
        if (inbox.add(message)) {
            App.print(String.format("Received via %s: %s", message.getLastSender(), message));

            ServentState.addPendingMessage(message, this);
            ServentState.checkPendingMessages(this);

            for (Servent neighbor : Config.LOCAL_SERVENT.getNeighbors()) {
                if (!message.containsSender(neighbor)) {
                    App.print(String.format("Redirecting %s from %s to %s", message.getType(), message.getSender(), neighbor));
                    App.send(message.setReceiver(neighbor).setSender());
                }
            }

            if (message.getType() == Message.Type.STOP) {
                ServentSingle.stop();
            }
        }
    }

    public void onPending() {
        switch (message.getType()) {
            case TRANSACTION:
                TransactionMessage transaction = (TransactionMessage) message;

                if (token != null && transaction.precedes(MessageHandler.token)) {
                    oldMessages.add(transaction);
                }

                break;
        }
    }

    public void onCommitted() {
        switch (message.getType()) {
            case TRANSACTION:
                TransactionMessage transaction = (TransactionMessage) message;

                if (transaction.getDestination().equals(Config.LOCAL_SERVENT)) {
                    ServentState.getSnapshotManager().plus(transaction.getAmount(), transaction.getSender());
                }

                break;
            case ASK:
                AskMessage ask = (AskMessage) message;

                token = ask;

                Snapshot snapshot = ServentState.getSnapshotManager().getSnapshot();
                ServentState.broadcast(new TellMessage(Config.LOCAL_SERVENT, Config.LOCAL_SERVENT, snapshot, ask.getSender()), collector);

                break;
            case TELL:
                TellMessage tell = (TellMessage) message;

                if (tell.getDestination().equals(Config.LOCAL_SERVENT)) {
                    collector.addSnapshot(tell.getSender(), tell.getSnapshot());
                }

                break;
            case TERMINATE:
                for (Servent servent : Config.SERVENTS) {
                    if (servent.equals(Config.LOCAL_SERVENT)) {
                        continue;
                    }

                    int plus = 0;
                    int minus = 0;

                    for (TransactionMessage m : oldMessages) {
                        if (m.getSender().equals(servent) && m.getDestination().equals(Config.LOCAL_SERVENT)) {
                            plus += m.getAmount();
                        } else if (m.getDestination().equals(servent) && m.getSender().equals(Config.LOCAL_SERVENT)) {
                            minus += m.getAmount();
                        }
                    }

                    if (plus > 0) {
                        App.print(String.format("Servent %s has %d unreceived bitcakes from %s", Config.LOCAL_SERVENT, plus, servent));
                    }

                    if (minus > 0) {
                        App.print(String.format("Servent %s has %d unreceived bitcakes from %s", servent, minus, Config.LOCAL_SERVENT));
                    }
                }

                oldMessages.clear();
                token = null;

                break;
        }
    }
}
