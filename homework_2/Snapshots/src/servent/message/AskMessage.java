package servent.message;

import app.ServentInfo;

public class AskMessage extends BasicMessage {

    private static final long serialVersionUID = 1L;

    public AskMessage(ServentInfo originalSenderInfo, ServentInfo receiverInfo) {
        super(MessageType.ASK, originalSenderInfo, receiverInfo, null);
    }
}
