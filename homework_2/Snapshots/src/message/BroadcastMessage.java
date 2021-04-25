package message;

import app.Config;
import app.Servent;
import app.ServentState;

import java.util.Map;

public class BroadcastMessage extends Message {

    private static final long serialVersionUID = 1L;

    private Map<Servent, Integer> clock;

    public BroadcastMessage(Message.Type type, String text, Servent sender, Servent receiver) {
        super(type, text, sender, receiver);

        clock = ServentState.getClockReceived();
    }

    public BroadcastMessage(String text) {
        this(Message.Type.BROADCAST, text, Config.LOCAL_SERVENT, Config.LOCAL_SERVENT);
    }

    public BroadcastMessage(BroadcastMessage m) {
        super(m);

        clock = m.clock;
    }

    @Override
    protected Message clone() {
        return new BroadcastMessage(this);
    }

    @Override
    public String toString() {
        return getType() + " of " + getText() + " with clock " + clock;
    }

    public Map<Servent, Integer> getClock() {
        return clock;
    }
}
