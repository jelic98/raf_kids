package servent.snapshot;

import app.AppConfig;
import app.Servent;
import servent.message.CausalBroadcastMessage;
import servent.message.Message;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

public class CausalBroadcastShared {

    private static final Map<Servent, Integer> vectorClock = new ConcurrentHashMap<>();
    private static final List<Message> committedMessages = new CopyOnWriteArrayList<>();
    private static final Queue<Message> pendingMessages = new ConcurrentLinkedQueue<>();
    private static final Object pendingMessagesLock = new Object();
    private static Servent askSender;

    public static void initializeVectorClock() {
        for (Servent servent : AppConfig.SERVENTS) {
            vectorClock.put(servent, 0);
        }
    }

    public static void incrementClock(Servent servent) {
        vectorClock.computeIfPresent(servent, (key, value) -> value + 1);
    }

    public static Map<Servent, Integer> getVectorClock() {
        return new ConcurrentHashMap<>(vectorClock);
    }

    public static List<Message> getCommittedMessages() {
        return new CopyOnWriteArrayList<>(committedMessages);
    }

    public static List<Message> getPendingMessages() {
        return new CopyOnWriteArrayList<>(pendingMessages);
    }

    public static void commitMessage(Message message, boolean checkPending) {
        committedMessages.add(message);
        incrementClock(message.getSender());

        if (checkPending) {
            checkPendingMessages();
        }

        String content = message.getText() == null ? message.getType().toString() : message.getText();

        AppConfig.print("Committed " + content);
    }

    public static void addPendingMessage(Message msg) {
        pendingMessages.add(msg);
    }

    private static boolean shouldCommit(Map<Servent, Integer> clock1, Map<Servent, Integer> clock2) {
        for (Servent servent : clock1.keySet()) {
            if (clock2.get(servent) > clock1.get(servent)) {
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
                Iterator<Message> i = pendingMessages.iterator();

                while (i.hasNext()) {
                    CausalBroadcastMessage message = (CausalBroadcastMessage) i.next();

                    if (shouldCommit(getVectorClock(), message.getClock())) {
                        commitMessage(message, false);

                        i.remove();

                        gotWork = true;

                        break;
                    }
                }
            }
        }
    }

    public static Servent getAskSender() {
        return askSender;
    }

    public static void setAskSender(Servent sender) {
        askSender = sender;
    }
}
