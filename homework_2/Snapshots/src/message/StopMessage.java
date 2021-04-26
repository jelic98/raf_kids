package message;

import app.Config;

public class StopMessage extends Message {

    private static final long serialVersionUID = 1L;

    public StopMessage() {
        super(Type.STOP, null, Config.LOCAL_SERVENT, Config.LOCAL_SERVENT);
    }

    public StopMessage(StopMessage m) {
        super(m);
    }

    @Override
    protected Message clone() {
        return new StopMessage(this);
    }

    @Override
    public String toString() {
        return getType() + " with clock " + getClock();
    }
}
