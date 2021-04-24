package servent.message;

import app.AppConfig;
import app.Servent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;

public class MessageUtil {

    private static final boolean MESSAGE_UTIL_PRINTING = false;
    private static final Random random = new Random();

    public static Message readMessage(Socket socket) {
        Message message = null;

        try {
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            message = (Message) ois.readObject();

            socket.close();
        } catch (Exception e) {
            AppConfig.error("Error while reading socket on " + socket.getInetAddress() + ":" + socket.getPort());
        }

        if (MESSAGE_UTIL_PRINTING) {
            AppConfig.print("Got message " + message);
        }

        return message;
    }

    public static void sendMessage(Message message) {
        try {
            Thread.sleep(random.nextInt(501) + 500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        Servent receiver = message.getReceiver();

        if (MESSAGE_UTIL_PRINTING) {
            AppConfig.print("Sending message " + message);
        }

        try {
            Socket sendSocket = new Socket(receiver.getIp(), receiver.getPort());

            ObjectOutputStream oos = new ObjectOutputStream(sendSocket.getOutputStream());
            oos.writeObject(message);
            oos.flush();

            sendSocket.close();

            message.sendEffect();
        } catch (IOException e) {
            AppConfig.error(String.format("Cannot send message %s (%s)", message, e.getMessage()));
        }
    }
}
