package servent.handler;

import servent.message.Message;
import servent.message.MessageType;
import servent.snapshot.BitcakeManager;

public class TransactionHandler implements Runnable {

    private Message clientMessage;
    private BitcakeManager bitcakeManager;

    public TransactionHandler(Message clientMessage, BitcakeManager bitcakeManager) {
        this.clientMessage = clientMessage;
        this.bitcakeManager = bitcakeManager;
    }

    @Override
    public void run() {
        if (clientMessage.getMessageType() == MessageType.TRANSACTION) {
            bitcakeManager.addSomeBitcakes(Integer.parseInt(clientMessage.getMessageText()));
        }
    }
}