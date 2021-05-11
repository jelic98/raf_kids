package message;

import app.App;
import app.Config;
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

    public Message(Type type, String text) {
        this(type, text, Config.LOCAL_SERVENT, Config.LOCAL_SERVENT);
    }

    public Message(Message m) {
        this(m.id, m.type, m.text, m.sender, m.receiver);

        route.addAll(m.route);
    }

    public Type getType() {
        return type;
    }

    public Servent getSender() {
        return sender;
    }

    public Message setSender() {
        Message message = copy();
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
        Message message = copy();
        message.receiver = receiver;

        return message;
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
        return type + " " + text;
    }

    protected abstract Message copy();
    protected abstract void handle(MessageHandler handler);

    public enum Type {
        BROADCAST,
        HAIL_ASK,
        HAIL_TELL,
        REGISTER_ASK,
        REGISTER_TELL,
        SORRY,
        PUBLISH,
        UPDATE,
        PULL_ASK,
        PULL_TELL,
        PUSH,
        STOP
    }
}
