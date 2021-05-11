package app;

import message.BroadcastMessage;
import message.MessageHandler;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

public class ServentState {

    private static final Map<Servent, Integer> clock = new ConcurrentHashMap<>();
    private static final List<BroadcastMessage> committedMessages = new CopyOnWriteArrayList<>();
    private static final Queue<BroadcastMessage> pendingMessages = new ConcurrentLinkedQueue<>();
    private static final Object pendingMessagesLock = new Object();

    public static Map<Servent, Integer> getClock() {
        return new ConcurrentHashMap<>(clock);
    }

    public static List<BroadcastMessage> getCommittedMessages() {
        return new CopyOnWriteArrayList<>(committedMessages);
    }

    public static List<BroadcastMessage> getPendingMessages() {
        return new CopyOnWriteArrayList<>(pendingMessages);
    }

    public static void initializeVectorClock() {
        for (Servent servent : Config.SERVENTS) {
            clock.put(servent, 0);
        }
    }

    public static Map<Servent, Integer> incrementClock(Servent Servent) {
        Map<Servent, Integer> clock = getClock();

        ServentState.clock.computeIfPresent(Servent, (k, v) -> v + 1);

        return clock;
    }

    private static boolean missedBroadcast(BroadcastMessage message) {
        for (Map.Entry<Servent, Integer> e : clock.entrySet()) {
            if (message.getClock().get(e.getKey()) > e.getValue()) {
                return true;
            }
        }

        return false;
    }

    public static void commitMessage(BroadcastMessage message, MessageHandler handler) {
        committedMessages.add(message);

        if (!message.getSender().equals(Config.LOCAL_SERVENT)) {
            incrementClock(message.getSender());
        }
    }

    public static void addPendingMessage(BroadcastMessage message) {
        pendingMessages.add(message);
    }

    public static void checkPendingMessages(MessageHandler handler) {
        boolean gotWork = true;

        while (gotWork) {
            gotWork = false;

            synchronized (pendingMessagesLock) {
                Iterator<BroadcastMessage> i = pendingMessages.iterator();

                while (i.hasNext()) {
                    BroadcastMessage message = i.next();

                    if (!missedBroadcast(message)) {
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
