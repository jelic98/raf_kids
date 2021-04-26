package app;

import message.Message;
import message.MessageHandler;
import snapshot.SnapshotManager;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

public class ServentState {

    private static final Map<Servent, Integer> clock = new ConcurrentHashMap<>();
    private static final List<Message> committedMessages = new CopyOnWriteArrayList<>();
    private static final Queue<Message> pendingMessages = new ConcurrentLinkedQueue<>();
    private static final SnapshotManager snapshotManager = new SnapshotManager();
    private static final Object pendingMessagesLock = new Object();

    public static Map<Servent, Integer> getClock() {
        return new ConcurrentHashMap<>(clock);
    }

    public static List<Message> getCommittedMessages() {
        return new CopyOnWriteArrayList<>(committedMessages);
    }

    public static List<Message> getPendingMessages() {
        return new CopyOnWriteArrayList<>(pendingMessages);
    }

    public static SnapshotManager getSnapshotManager() {
        return snapshotManager;
    }

    public static void initializeVectorClock() {
        for (Servent servent : Config.SERVENTS) {
            clock.put(servent, 0);
        }
    }

    public static void incrementClock(Servent servent) {
        clock.computeIfPresent(servent, (k, v) -> v + 1);
    }

    public static boolean shouldCommit(Message message) {
        for (Map.Entry<Servent, Integer> e : clock.entrySet()) {
            if (message.getClock().get(e.getKey()) > e.getValue()) {
                return false;
            }
        }

        return true;
    }

    public static void commitMessage(Message message, MessageHandler handler) {
        committedMessages.add(message);

        incrementClock(message.getSender());

        handler.onCommitted(message);
    }

    public static void addPendingMessage(Message message) {
        pendingMessages.add(message);
    }

    public static void checkPendingMessages(MessageHandler handler) {
        boolean gotWork = true;

        while (gotWork) {
            gotWork = false;

            synchronized (pendingMessagesLock) {
                Iterator<Message> i = pendingMessages.iterator();

                while (i.hasNext()) {
                    Message message = i.next();

                    if (shouldCommit(message)) {
                        commitMessage(message, handler);
                        gotWork = true;
                        i.remove();
                        break;
                    }
                }
            }
        }
    }
}
