package message;

import app.*;
import snapshot.Snapshot;
import snapshot.SnapshotCollector;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class MessageHandler implements Runnable {

    private static final int INBOX_SIZE = 10;

    private static MessageHandler instance;

    private final SnapshotCollector collector;
    private final BlockingQueue<Message> inbox;
    private final Set<Message> history;
    private AskMessage token;

    public MessageHandler(SnapshotCollector collector) {
        this.collector = collector;

        inbox = new ArrayBlockingQueue<>(INBOX_SIZE);
        history = Collections.newSetFromMap(new ConcurrentHashMap<>());

        instance = this;
    }

    public static void handle(Message message) {
        try {
            instance.inbox.put(message);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                onReceived(inbox.take());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private void onReceived(Message message) throws InterruptedException {
        if (history.add(message)) {
            App.print(String.format("Received via %s: %s", message.getLastSender(), message));

            ServentState.addPendingMessage(message);
            ServentState.checkPendingMessages(this);

            for (Servent neighbor : Config.LOCAL_SERVENT.getNeighbors()) {
                if (!message.containsSender(neighbor)) {
                    App.print(String.format("Redirecting %s from %s to %s", message.getType(), message.getSender(), neighbor));
                    App.send(message.setReceiver(neighbor).setSender());
                }
            }

            if (message.getType() == Message.Type.STOP) {
                ServentSingle.stop();
                throw new InterruptedException();
            }
        }
    }

    public void onCommitted(Message message) {
        switch (message.getType()) {
            case TRANSACTION:
                onTransaction(message);
                break;
            case ASK:
                onAsk(message);
                break;
            case TELL:
                onTell(message);
                break;
            case TERMINATE:
                onTerminate(message);
                break;
        }
    }

    private void onTransaction(Message message) {
        TransactionMessage transaction = (TransactionMessage) message;

        if (transaction.getDestination().equals(Config.LOCAL_SERVENT)) {
            ServentState.getSnapshotManager().plus(transaction.getAmount(), transaction.getSender());
        }
    }

    private void onAsk(Message message) {
        AskMessage ask = (AskMessage) message;

        token = ask;

        Snapshot snapshot = ServentState.getSnapshotManager().getSnapshot();
        handle(new TellMessage(snapshot, ask.getSender()));
    }

    private void onTell(Message message) {
        TellMessage tell = (TellMessage) message;

        if (tell.getDestination().equals(Config.LOCAL_SERVENT)) {
            collector.addSnapshot(tell.getSender(), tell.getSnapshot());
        }
    }

    private void onTerminate(Message message) {
        TerminateMessage terminate = (TerminateMessage) message;

        Map<Servent, Snapshot> state = terminate.getState();
        Snapshot snapshot = state.get(Config.LOCAL_SERVENT);
        List<Message> messageHistory = snapshot.getMessageHistory();

        for (Servent servent : Config.SERVENTS) {
            if (servent.equals(Config.LOCAL_SERVENT)) {
                continue;
            }

            int diff = 0;

            for (Message m : ServentState.getCommittedMessages()) {
                if (m.getType() == Message.Type.TRANSACTION) {
                    TransactionMessage t = (TransactionMessage) m;

                    if (t.precedes(token) && !messageHistory.contains(t) && t.getSender().equals(servent) && t.getDestination().equals(Config.LOCAL_SERVENT)) {
                        diff += t.getAmount();
                    }
                }
            }

            if (diff > 0) {
                App.print(String.format("Servent %s has %d unreceived bitcakes from %s", Config.LOCAL_SERVENT, diff, servent));
            }
        }

        token = null;
    }
}
