package servent.message;

import app.Servent;

public class TellMessage extends BasicMessage {

    private static final long serialVersionUID = 1L;

    public TellMessage(Servent sender, Servent receiver, int amount) {
        super(MessageType.TELL, String.valueOf(amount), sender, receiver);
    }
}
