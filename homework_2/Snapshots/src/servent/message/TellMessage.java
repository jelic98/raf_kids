package servent.message;

import app.Servent;

import java.util.List;
import java.util.Map;

public class TellMessage extends CausalBroadcastMessage {

    private static final long serialVersionUID = 1L;

    public TellMessage(Servent sender, Servent receiver, int amount, Map<Servent, Integer> clock) {
        super(MessageType.TELL, sender, receiver, String.valueOf(amount), clock);
    }

    private TellMessage(Servent sender, Servent receiver, String text, List<Servent> route, int messageId, Map<Servent, Integer> clock) {
        super(MessageType.TELL, sender, receiver, route, text, messageId, clock);
    }

    @Override
    protected Message createInstance(MessageType type, String text, Servent sender, Servent receiver, List<Servent> route, int messageId) {
        return new TellMessage(sender, receiver, text, route, messageId, getClock());
    }
}
