package servent.message;

import app.AppConfig;
import app.Servent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class BasicMessage implements Message {

    private static final long serialVersionUID = 1L;
    private static final AtomicInteger messageCounter = new AtomicInteger(0);

    private final MessageType type;
    private final String text;
    private final Servent sender;
    private final Servent receiver;
    private final List<Servent> route;
    private final int messageId;

    public BasicMessage(MessageType type, String text, Servent sender, Servent receiver) {
        this.type = type;
        this.text = text;
        this.sender = sender;
        this.receiver = receiver;

        route = new ArrayList<>();
        messageId = messageCounter.getAndIncrement();
    }

    protected BasicMessage(MessageType type, String text, Servent sender, Servent receiver, List<Servent> route, int messageId) {
        this.type = type;
        this.text = text;
        this.sender = sender;
        this.receiver = receiver;
        this.route = route;
        this.messageId = messageId;
    }

    @Override
    public MessageType getType() {
        return type;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public Servent getSender() {
        return sender;
    }

    @Override
    public Message setSender() {
        List<Servent> route = new ArrayList<>(this.route);
        route.add(AppConfig.LOCAL_SERVENT);

        return createInstance(type, text, sender, receiver, route, messageId);
    }

    @Override
    public Servent getLastSender() {
        int size = route.size();

        if (size == 0) {
            return sender;
        }

        return route.get(size - 1);
    }

    @Override
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

    @Override
    public Servent getReceiver() {
        return receiver;
    }

    @Override
    public Message setReceiver(Servent receiver) {
        if (AppConfig.LOCAL_SERVENT.getNeighbors().contains(receiver) || receiver.equals(AppConfig.LOCAL_SERVENT)) {
            return createInstance(type, text, sender, receiver, route, messageId);
        } else {
            AppConfig.error("Servent " + receiver + " is not a neighbor");

            return null;
        }
    }

    @Override
    public void sendEffect() {

    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BasicMessage) {
            BasicMessage m = (BasicMessage) obj;
            return messageId == m.messageId && sender.equals(m.sender);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageId, sender);
    }

    @Override
    public String toString() {
        return String.format("[%s|%d|%s|%s|%s]",
                sender,
                messageId,
                text,
                type,
                receiver);
    }

    protected Message createInstance(MessageType type, String text, Servent sender, Servent receiver,
                                     List<Servent> route, int messageId) {
        return new BasicMessage(type, text, sender, receiver, route, messageId);
    }
}
