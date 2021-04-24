package servent.message;

import app.Servent;
import servent.snapshot.BitcakeManager;

import java.util.List;
import java.util.Map;

public class TransactionMessage extends CausalBroadcastMessage {

    private static final long serialVersionUID = 1L;

    private transient BitcakeManager bitcakeManager;

    public TransactionMessage(Servent sender, Servent receiver, int amount, BitcakeManager bitcakeManager, Map<Servent, Integer> clock) {
        super(MessageType.TRANSACTION, sender, receiver, String.valueOf(amount), clock);
        this.bitcakeManager = bitcakeManager;
    }

    private TransactionMessage(Servent sender, Servent receiver, String text, List<Servent> route, int messageId, Map<Servent, Integer> clock) {
        super(MessageType.TRANSACTION, sender, receiver, route, text, messageId, clock);
    }

    @Override
    protected Message createInstance(MessageType type, String text, Servent sender, Servent receiver, List<Servent> route, int messageId) {
        return new TransactionMessage(sender, receiver, text, route, messageId, getClock());
    }

    @Override
    public void sendEffect() {
        bitcakeManager.takeBitcakes(Integer.parseInt(getText()));
    }
}



