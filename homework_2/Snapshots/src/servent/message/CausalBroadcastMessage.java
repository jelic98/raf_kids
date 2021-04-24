package servent.message;

import app.Servent;

import java.util.List;
import java.util.Map;

public class CausalBroadcastMessage extends BasicMessage {

    private static final long serialVersionUID = 1L;
    private final Map<Servent, Integer> clock;

    public CausalBroadcastMessage(Servent sender, Servent receiver, String text, Map<Servent, Integer> clock) {
        this(MessageType.CAUSAL_BROADCAST, sender, receiver, text, clock);
    }

    protected CausalBroadcastMessage(MessageType messageType, Servent sender, Servent receiver, String text, Map<Servent, Integer> clock) {
        super(messageType, text, sender, receiver);

        this.clock = clock;
    }

    private CausalBroadcastMessage(Servent sender, Servent receiver, List<Servent> route, String text, int messageId, Map<Servent, Integer> clock) {
        this(MessageType.CAUSAL_BROADCAST, sender, receiver, route, text, messageId, clock);
    }

    protected CausalBroadcastMessage(MessageType type, Servent sender, Servent receiver, List<Servent> route, String text, int messageId, Map<Servent, Integer> clock) {
        super(type, text, sender, receiver, route, messageId);

        this.clock = clock;
    }

    @Override
    protected Message createInstance(MessageType type, String text, Servent sender, Servent receiver, List<Servent> route, int messageId) {
        return new CausalBroadcastMessage(sender, receiver, route, text, messageId, clock);
    }

    public Map<Servent, Integer> getClock() {
        return clock;
    }
}
