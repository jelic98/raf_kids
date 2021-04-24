package servent.message;

import app.Servent;

public class AskMessage extends CausalBroadcastMessage {

    private static final long serialVersionUID = 1L;

    public AskMessage(Servent sender) {
        super(MessageType.ASK, null, sender, null);
    }

    public AskMessage(AskMessage m) {
        super(m);
    }

    @Override
    protected Message clone() {
        return new AskMessage(this);
    }
}
