package app;

import message.BroadcastMessage;
import snapshot.SnapshotManager;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

public class ServentState {

    private static final Map<Servent, Integer> clockReceived = new ConcurrentHashMap<>();
    private static final List<BroadcastMessage> committedMessages = new CopyOnWriteArrayList<>();
    private static final Queue<BroadcastMessage> pendingMessages = new ConcurrentLinkedQueue<>();
    private static final SnapshotManager snapshotManager = new SnapshotManager();
    private static final Object pendingMessagesLock = new Object();

    public static Map<Servent, Integer> getClockReceived() {
        return new ConcurrentHashMap<>(clockReceived);
    }

    public static List<BroadcastMessage> getCommittedMessages() {
        return new CopyOnWriteArrayList<>(committedMessages);
    }

    public static List<BroadcastMessage> getPendingMessages() {
        return new CopyOnWriteArrayList<>(pendingMessages);
    }

    public static SnapshotManager getSnapshotManager() {
        return snapshotManager;
    }

    public static void initializeVectorClock() {
        for (Servent servent : Config.SERVENTS) {
            clockReceived.put(servent, 0);
        }
    }

    public static void incrementClockReceived(Servent servent) {
        clockReceived.computeIfPresent(servent, (k, v) -> v + 1);
    }

    public static void commitMessage(BroadcastMessage message, boolean checkPending) {
        committedMessages.add(message);

        incrementClockReceived(message.getSender());

        if (checkPending) {
            checkPendingMessages();
        }
    }

    public static void addPendingMessage(BroadcastMessage msg) {
        pendingMessages.add(msg);
    }

    private static boolean shouldCommit(BroadcastMessage message) {
        for (Servent servent : clockReceived.keySet()) {
            if (message.getClock().get(servent) > clockReceived.get(servent)) {
                return false;
            }
        }

        return true;
    }

    public static void checkPendingMessages() {
        boolean gotWork = true;

        while (gotWork) {
            gotWork = false;

            synchronized (pendingMessagesLock) {
                Iterator<BroadcastMessage> i = pendingMessages.iterator();

                while (i.hasNext()) {
                    BroadcastMessage message = i.next();

                    if (shouldCommit(message)) {
                        commitMessage(message, false);

                        i.remove();

                        gotWork = true;

                        break;
                    }
                }
            }
        }
    }

    public static void broadcast(BroadcastMessage message) {
        commitMessage(message, true);

        App.print("Broadcasting: " + message);

        for (Servent neighbor : Config.LOCAL_SERVENT.getNeighbors()) {
            App.send(message.setReceiver(neighbor));
        }
    }
}
