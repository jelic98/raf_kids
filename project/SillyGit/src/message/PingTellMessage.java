package message;

import app.Config;
import servent.Servent;

public class PingTellMessage extends Message {

    private static final long serialVersionUID = 1L;

    public PingTellMessage(Servent receiver) {
        super(null, Config.LOCAL, receiver);
    }

    public PingTellMessage(PingTellMessage m) {
        super(m);
    }

    @Override
    protected Message copy() {
        return new PingTellMessage(this);
    }

    @Override
    protected void handle() {
        Config.SYSTEM.addServent(getSender());
    }
}
