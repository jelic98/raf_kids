package servent.snapshot;

import app.AppConfig;
import app.Servent;
import servent.message.CausalBroadcastMessage;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

public class CausalBroadcastShared {

    private static final Map<Servent, Integer> clockReceived = new ConcurrentHashMap<>();
    private static final Map<Servent, Integer> clockSent = new ConcurrentHashMap<>();
    private static final List<CausalBroadcastMessage> committedMessages = new CopyOnWriteArrayList<>();
    private static final Queue<CausalBroadcastMessage> pendingMessages = new ConcurrentLinkedQueue<>();
    private static final Object pendingMessagesLock = new Object();
    private static Servent askSender;

    public static void initializeVectorClock() {
        for (Servent servent : AppConfig.SERVENTS) {
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

    public static List<CausalBroadcastMessage> getCommittedMessages() {
        return new CopyOnWriteArrayList<>(committedMessages);
    }

    public static List<CausalBroadcastMessage> getPendingMessages() {
        return new CopyOnWriteArrayList<>(pendingMessages);
    }

    public static void commitMessage(CausalBroadcastMessage message, boolean checkPending) {
        committedMessages.add(message);
        incrementClockReceived(message.getSender());

        String content = message.getText() == null ? message.getType().toString() : message.getText();

        AppConfig.print("Committed " + content);

        if (checkPending) {
            checkPendingMessages();
        }
    }

    public static void addPendingMessage(CausalBroadcastMessage msg) {
        pendingMessages.add(msg);
    }

    private static boolean shouldCommit(CausalBroadcastMessage message) {
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
                Iterator<CausalBroadcastMessage> i = pendingMessages.iterator();

                while (i.hasNext()) {
                    CausalBroadcastMessage message = i.next();

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

    public static Servent getAskSender() {
        return askSender;
    }

    public static void setAskSender(Servent sender) {
        askSender = sender;
    }
}
