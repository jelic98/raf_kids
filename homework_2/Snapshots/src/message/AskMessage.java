package message;

import app.Servent;

public class AskMessage extends BroadcastMessage {

    private static final long serialVersionUID = 1L;

    public AskMessage(Servent sender) {
        super(Message.Type.ASK, null, sender, null);
    }

    public AskMessage(AskMessage m) {
        super(m);
    }

    @Override
    protected Message clone() {
        return new AskMessage(this);
    }

    @Override
    public String toString() {
        return getType().toString() + " " + getClock();
    }
}
