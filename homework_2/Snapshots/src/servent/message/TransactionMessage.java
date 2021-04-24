package servent.message;

import app.Servent;
import servent.snapshot.BitcakeManager;

public class TransactionMessage extends BasicMessage {

    private static final long serialVersionUID = 1L;

    private transient BitcakeManager bitcakeManager;

    public TransactionMessage(Servent sender, Servent receiver, int amount, BitcakeManager bitcakeManager) {
        super(MessageType.TRANSACTION, String.valueOf(amount), sender, receiver);
        this.bitcakeManager = bitcakeManager;
    }

    @Override
    public void sendEffect() {
        bitcakeManager.takeBitcakes(Integer.parseInt(getText()));
    }
}
