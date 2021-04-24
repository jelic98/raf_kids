package servent.handler;

import servent.message.Message;
import servent.message.MessageType;
import servent.snapshot.BitcakeManager;

public class TransactionHandler implements Runnable {

    private Message message;
    private BitcakeManager bitcakeManager;

    public TransactionHandler(Message message, BitcakeManager bitcakeManager) {
        this.message = message;
        this.bitcakeManager = bitcakeManager;
    }

    @Override
    public void run() {
        if (message.getType() == MessageType.TRANSACTION) {
            bitcakeManager.addBitcakes(Integer.parseInt(message.getText()));
        }
    }
}