package servent.message;

import app.AppConfig;
import app.Servent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Message implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final AtomicInteger messageCounter = new AtomicInteger(0);

    private int id;
    private MessageType type;
    private String text;
    private Servent sender;
    private Servent receiver;
    private List<Servent> route;

    public Message(MessageType type, String text, Servent sender, Servent receiver) {
        this.type = type;
        this.text = text;
        this.sender = sender;
        this.receiver = receiver;

        id = messageCounter.getAndIncrement();
        route = new ArrayList<>();
    }

    public Message(Message m) {
        this(m.type, m.text, m.sender, m.receiver);

        id = m.id;
        route.addAll(m.route);
    }

    public MessageType getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public Servent getSender() {
        return sender;
    }

    public Message setSender() {
        Message message = clone();
        message.route.add(AppConfig.LOCAL_SERVENT);

        return message;
    }

    public Servent getLastSender() {
        int size = route.size();

        if (size == 0) {
            return sender;
        }

        return route.get(size - 1);
    }

    public boolean containsSender(Servent sender) {
        if (route.isEmpty()) {
            return this.sender.equals(sender);
        }

        for (Servent s : route) {
            if (s.equals(sender)) {
                return true;
            }
        }

        return false;
    }

    public Servent getReceiver() {
        return receiver;
    }

    public Message setReceiver(Servent receiver) {
        if (AppConfig.LOCAL_SERVENT.getNeighbors().contains(receiver) || receiver.equals(AppConfig.LOCAL_SERVENT)) {
            Message message = clone();
            message.receiver = receiver;

            return message;
        } else {
            AppConfig.error("Servent " + receiver + " is not a neighbor");

            return null;
        }
    }

    public void sendEffect() {

    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Message) {
            Message m = (Message) obj;
            return id == m.id && sender.equals(m.sender);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sender);
    }

    @Override
    public String toString() {
        return String.format("[%s|%d|%s|%s|%s]", sender, id, text, type, receiver);
    }

    protected abstract Message clone();
}
