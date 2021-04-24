package app;

import message.BroadcastMessage;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

public class ServentState {

    private static final Map<Servent, Integer> clockReceived = new ConcurrentHashMap<>();
    private static final Map<Servent, Integer> clockSent = new ConcurrentHashMap<>();
    private static final List<BroadcastMessage> committedMessages = new CopyOnWriteArrayList<>();
    private static final Queue<BroadcastMessage> pendingMessages = new ConcurrentLinkedQueue<>();
    private static final Object pendingMessagesLock = new Object();

    public static void initializeVectorClock() {
        for (Servent servent : Config.SERVENTS) {
            clockReceived.put(servent, 0);
            clockSent.put(servent, 0);
        }
    }

    public static void incrementClockReceived(Servent servent) {
        clockReceived.computeIfPresent(servent, (key, value) -> value + 1);
    }

    public static void incrementClockSent(Servent servent) {
        clockSent.computeIfPresent(servent, (key, value) -> value + 1);
    }

    public static Map<Servent, Integer> getClockReceived() {
        return new ConcurrentHashMap<>(clockReceived);
    }

    public static Map<Servent, Integer> getClockSent() {
        return new ConcurrentHashMap<>(clockSent);
    }

    public static List<BroadcastMessage> getCommittedMessages() {
        return new CopyOnWriteArrayList<>(committedMessages);
    }

    public static List<BroadcastMessage> getPendingMessages() {
        return new CopyOnWriteArrayList<>(pendingMessages);
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
}
