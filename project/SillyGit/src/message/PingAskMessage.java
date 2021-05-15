package message;

import app.App;
import app.Config;
import servent.Servent;

public class PingAskMessage extends Message {

    private static final long serialVersionUID = 1L;

    public PingAskMessage(Servent receiver) {
        super(null, Config.LOCAL, receiver);
    }

    public PingAskMessage(PingAskMessage m) {
        super(m);
    }

    @Override
    protected Message copy() {
        return new PingAskMessage(this);
    }

    @Override
    protected void handle() {
        Config.SYSTEM.addServent(getSender());
        // TODO PING messages (after joining system) should be synchronous of asynchronous?
        App.send(new PingTellMessage(getSender()));
    }
}

