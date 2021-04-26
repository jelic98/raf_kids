package message;

import app.Config;

public class AskMessage extends Message {

    private static final long serialVersionUID = 1L;

    public AskMessage() {
        super(Message.Type.ASK, null, Config.LOCAL_SERVENT, Config.LOCAL_SERVENT);
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
        return getType() + " with clock " + getClock();
    }
}
