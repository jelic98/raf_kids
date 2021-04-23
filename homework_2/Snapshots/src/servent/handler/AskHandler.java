package servent.handler;

import servent.message.Message;
import servent.message.MessageType;
import servent.message.MessageUtil;
import servent.message.TellMessage;
import servent.snapshot.BitcakeManager;

public class AskHandler implements Runnable {

    private Message clientMessage;
    private BitcakeManager bitcakeManager;

    public AskHandler(Message clientMessage, BitcakeManager bitcakeManager) {
        this.clientMessage = clientMessage;
        this.bitcakeManager = bitcakeManager;
    }

    @Override
    public void run() {
        if (clientMessage.getMessageType() == MessageType.ASK) {
            MessageUtil.sendMessage(new TellMessage(clientMessage.getReceiverInfo(),
                    clientMessage.getOriginalSenderInfo(),
                    bitcakeManager.getCurrentBitcakeAmount()));
        }
    }
}
