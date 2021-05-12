package app;

import message.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class App {

    private static final boolean MESSAGE_UTIL_PRINTING = true;
    private static final Random random = new Random();

    public static Message read(Socket socket) {
        Message message = null;

        try {
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            message = (Message) ois.readObject();

            socket.close();
        } catch (Exception e) {
            error(String.format("Error while reading socket on %s:%d (%s)" + socket.getInetAddress(), socket.getPort(), e.getMessage()));
        }

        if (MESSAGE_UTIL_PRINTING) {
            if(message == null) {
                print("Received NULL message");
            }else {
                print(String.format("Incoming: %s (%s->%s)", message, message.getSender(), message.getReceiver()));
            }
        }

        return message;
    }

    public static void send(Message message) {
        new Thread(() -> {
            try {
                Thread.sleep(random.nextInt(501) + 500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            Servent receiver = message.getReceiver();

            if (MESSAGE_UTIL_PRINTING) {
                print(String.format("Outgoing: %s (%s->%s)", message, message.getSender(), message.getReceiver()));
            }

            try {
                Address address = receiver.getAddress();
                Socket sendSocket = new Socket(address.getHost(), address.getPort());

                ObjectOutputStream oos = new ObjectOutputStream(sendSocket.getOutputStream());
                oos.writeObject(message);
                oos.flush();

                sendSocket.close();
            } catch (IOException e) {
                error(String.format("Cannot send message %s (%s)", message, e.getMessage()));
            }
        }).start();
    }

    public static void print(String message) {
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        Date now = new Date();

        System.out.println(timeFormat.format(now) + " - " + message);
    }

    public static void error(String message) {
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        Date now = new Date();

        System.err.println(timeFormat.format(now) + " - " + message);
    }
}
