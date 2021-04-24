package message;

import app.App;
import app.Config;
import app.Servent;
import app.ServentState;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Message implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final AtomicInteger messageCounter = new AtomicInteger(0);

    public enum Type {
        BROADCAST,
        TRANSACTION,
        ASK,
        TELL
    }

    private int id;
    private Type type;
    private String text;
    private Servent sender;
    private Servent receiver;
    private List<Servent> route;

    public Message(int id, Type type, String text, Servent sender, Servent receiver) {
        this.id = id;
        this.type = type;
        this.text = text;
        this.sender = sender;
        this.receiver = receiver;

        route = new ArrayList<>();
        route.add(sender);
    }

    public Message(Type type, String text, Servent sender, Servent receiver) {
        this(messageCounter.getAndIncrement(), type, text, sender, receiver);
    }

    public Message(Message m) {
        this(m.id, m.type, m.text, m.sender, m.receiver);

        route.addAll(m.route);
    }

    public Type getType() {
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
        message.route.add(Config.LOCAL_SERVENT);

        return message;
    }

    public Servent getLastSender() {
        return route.get(route.size() - 1);
    }

    public boolean containsSender(Servent sender) {
        return route.contains(sender);
    }

    public Servent getReceiver() {
        return receiver;
    }

    public Message setReceiver(Servent receiver) {
        if (Config.LOCAL_SERVENT.getNeighbors().contains(receiver) || receiver.equals(Config.LOCAL_SERVENT)) {
            Message message = clone();
            message.receiver = receiver;

            return message;
        } else {
            App.error("Servent " + receiver + " is not a neighbor");

            return null;
        }
    }

    public void sendEffect() {
        ServentState.incrementClockSent(receiver);
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
