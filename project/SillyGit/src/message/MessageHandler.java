package message;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class MessageHandler implements Runnable {

    private static final int INBOX_SIZE = 10;
    private static final BlockingQueue<Message> inbox = new ArrayBlockingQueue<>(INBOX_SIZE);
    private static volatile boolean working = true;

    public static void handle(Message message) {
        try {
            inbox.put(message);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void run() {
        while (working) {
            try {
                inbox.take().handle();
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
