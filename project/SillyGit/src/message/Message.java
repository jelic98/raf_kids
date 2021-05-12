package message;

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

    private final int id;
    private final String text;
    private final Servent sender;
    private Servent receiver;
    private final List<Servent> route;

    public Message(int id, String text, Servent sender, Servent receiver) {
        this.id = id;
        this.text = text;
        this.sender = sender;
        this.receiver = receiver;

        route = new ArrayList<>();
        route.add(sender);
    }

    public Message(String text, Servent sender, Servent receiver) {
        this(messageCounter.getAndIncrement(), text, sender, receiver);
    }

    public Message(String text) {
        this(text, Config.LOCAL, Config.LOCAL);
    }

    public Message(Message m) {
        this(m.id, m.text, m.sender, m.receiver);

        route.addAll(m.route);
    }

    public Servent getSender() {
        return sender;
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

    public Message redirect(Servent receiver) {
        Message message = copy();
        message.receiver = receiver;
        message.route.add(Config.LOCAL);

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
        String type = getClass().getSimpleName().replace("Message", "").replaceAll("([a-z])([A-Z]+)", "$1_$2").toUpperCase();

        if (text == null) {
            return type;
        } else {
            return type + " " + text;
        }
    }

    protected abstract Message copy();

    protected abstract void handle();
}
