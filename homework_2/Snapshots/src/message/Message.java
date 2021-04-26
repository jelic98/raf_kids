package message;

import app.App;
import app.Config;
import app.Servent;
import app.ServentState;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class Message implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final AtomicInteger messageCounter = new AtomicInteger(0);

    private int id;
    private Type type;
    private String text;
    private Servent sender;
    private Servent receiver;
    private List<Servent> route;
    private Map<Servent, Integer> clock;

    public Message(int id, Type type, String text, Servent sender, Servent receiver, Map<Servent, Integer> clock) {
        this.id = id;
        this.type = type;
        this.text = text;
        this.sender = sender;
        this.receiver = receiver;
        this.clock = clock;

        route = new ArrayList<>();
        route.add(sender);
    }

    public Message(Type type, String text, Servent sender, Servent receiver) {
        this(messageCounter.getAndIncrement(), type, text, sender, receiver, ServentState.getClock());
    }

    public Message(Message m) {
        this(m.id, m.type, m.text, m.sender, m.receiver, m.clock);

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
        if (Config.LOCAL_SERVENT.isNeighbor(receiver) || receiver.equals(Config.LOCAL_SERVENT)) {
            Message message = clone();
            message.receiver = receiver;

            return message;
        } else {
            App.error("Servent " + receiver + " is not a neighbor");

            return null;
        }
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
        return String.format("[%s|%d|%s|%s|%s] %s", sender, id, text, type, receiver, clone());
    }

    public Map<Servent, Integer> getClock() {
        return clock;
    }

    public boolean precedes(Message message) {
        Servent sender = message.getSender();

        return clock.get(sender) <= message.clock.get(sender);
    }

    protected Message clone() {
        return new Message(this);
    }

    public enum Type {
        BROADCAST,
        TRANSACTION,
        ASK,
        TELL,
        TERMINATE,
        STOP
    }
}
