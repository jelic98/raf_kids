package servent.message;

import app.Servent;

import java.util.List;
import java.util.Map;

public class AskMessage extends CausalBroadcastMessage {

    private static final long serialVersionUID = 1L;

    public AskMessage(Servent sender, Servent receiver, Map<Servent, Integer> clock) {
        super(MessageType.ASK, sender, receiver, null, clock);
    }

    private AskMessage(Servent sender, Servent receiver, List<Servent> route, int messageId, Map<Servent, Integer> clock) {
        super(MessageType.ASK, sender, receiver, route, null, messageId, clock);
    }

    @Override
    protected Message createInstance(MessageType type, String text, Servent sender, Servent receiver, List<Servent> route, int messageId) {
        return new AskMessage(sender, receiver, route, messageId, getClock());
    }
}
