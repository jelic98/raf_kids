package message;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class MessageHandler implements Runnable {

    private static final int INBOX_SIZE = 10;

    private static MessageHandler instance;

    private final BlockingQueue<Message> inbox;
    private volatile boolean working = true;

    public MessageHandler() {
        inbox = new ArrayBlockingQueue<>(INBOX_SIZE);

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
        while (working) {
            try {
                inbox.take().handle(this);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public void stop() {
        working = false;
    }
}
