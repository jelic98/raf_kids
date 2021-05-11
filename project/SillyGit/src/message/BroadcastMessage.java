package message;

import app.*;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class BroadcastMessage extends Message {

    private static final long serialVersionUID = 1L;
    private static final Set<BroadcastMessage> history = Collections.newSetFromMap(new ConcurrentHashMap<>());

    private Map<Servent, Integer> clock;

    public BroadcastMessage(Type type, String text) {
        super(type, text);

        clock = ServentState.incrementClock(Config.LOCAL_SERVENT);
    }

    public BroadcastMessage(String text) {
        this(Type.BROADCAST, text);
    }

    public BroadcastMessage(BroadcastMessage m) {
        super(m);

        clock = m.clock;
    }

    @Override
    protected Message copy() {
        return new BroadcastMessage(this);
    }

    @Override
    protected void handle(MessageHandler handler) {
        if (history.add(this)) {
            App.print(String.format("Received via %s: %s", getLastSender(), this));

            ServentState.addPendingMessage(this);
            ServentState.checkPendingMessages(handler);

            for (Servent neighbor : Config.LOCAL_SERVENT.getNeighbors()) {
                if (!containsSender(neighbor)) {
                    App.print(String.format("Redirecting %s from %s to %s", getType(), getSender(), neighbor));
                    App.send(setReceiver(neighbor).setSender());
                }
            }

            if (getType() == Message.Type.STOP) {
                ServentSingle.stop();
                handler.stop();
            }
        }
    }

    @Override
    public String toString() {
        return getType() + " with clock " + getClock();
    }

    public Map<Servent, Integer> getClock() {
        return clock;
    }
}

