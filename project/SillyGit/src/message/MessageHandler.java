package message;

import app.*;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class MessageHandler implements Runnable {

    private static final int INBOX_SIZE = 10;

    private static MessageHandler instance;

    private final BlockingQueue<Message> inbox;
    private final Set<Message> history;

    public MessageHandler() {
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
}
