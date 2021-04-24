package servent.message;

import app.AppConfig;
import app.Servent;
import servent.snapshot.BroadcastShared;

import java.util.Map;

public class BroadcastMessage extends Message {

    private static final long serialVersionUID = 1L;

    private Map<Servent, Integer> clock;

    public BroadcastMessage(MessageType type, String text, Servent sender, Servent receiver) {
        super(type, text, sender, receiver);

        clock = BroadcastShared.getClockReceived();
    }

    public BroadcastMessage(String text) {
        this(MessageType.BROADCAST, text, AppConfig.LOCAL_SERVENT, null);
    }

    public BroadcastMessage(BroadcastMessage m) {
        super(m);

        clock = m.clock;
    }

    @Override
    protected Message clone() {
        return new BroadcastMessage(this);
    }

    public Map<Servent, Integer> getClock() {
        return clock;
    }
}
