package message;

import app.Config;

public class TerminateMessage extends Message {

    private static final long serialVersionUID = 1L;

    public TerminateMessage() {
        super(Type.TERMINATE, null, Config.LOCAL_SERVENT, Config.LOCAL_SERVENT);
    }

    public TerminateMessage(TerminateMessage m) {
        super(m);
    }

    @Override
    protected Message clone() {
        return new TerminateMessage(this);
    }

    @Override
    public String toString() {
        return getType() + " with clock " + getClock();
    }
}
