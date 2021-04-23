package servent.message;

import app.ServentInfo;

public class TellMessage extends BasicMessage {

    private static final long serialVersionUID = 1L;

    public TellMessage(ServentInfo originalSenderInfo, ServentInfo receiverInfo, int amount) {
        super(MessageType.TELL, originalSenderInfo, receiverInfo, String.valueOf(amount));
    }
}
