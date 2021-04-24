package servent.message;

import app.AppConfig;
import app.Servent;
import servent.snapshot.CausalBroadcastShared;
import java.util.Map;

public class CausalBroadcastMessage extends Message {

    private static final long serialVersionUID = 1L;

    private Map<Servent, Integer> clock;

    public CausalBroadcastMessage(MessageType type, String text, Servent sender, Servent receiver) {
        super(type, text, sender, receiver);

        clock = CausalBroadcastShared.getClockReceived();
    }

    public CausalBroadcastMessage(String text) {
        this(MessageType.CAUSAL_BROADCAST, text, AppConfig.LOCAL_SERVENT, null);
    }

    public CausalBroadcastMessage(CausalBroadcastMessage m) {
        super(m);

        clock = m.clock;
    }

    @Override
    protected Message clone() {
        return new CausalBroadcastMessage(this);
    }

    public Map<Servent, Integer> getClock() {
        return clock;
    }
}
