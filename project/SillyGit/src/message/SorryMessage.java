package message;

import app.App;
import app.Config;
import servent.Servent;
import app.ServentSingle;

public class SorryMessage extends Message {

    private static final long serialVersionUID = 1L;

    public SorryMessage(Servent receiver) {
        super(null, Config.LOCAL, receiver);
    }

    public SorryMessage(SorryMessage m) {
        super(m);
    }

    @Override
    protected Message copy() {
        return new SorryMessage(this);
    }

    @Override
    protected void handle() {
        App.error("Cannot enter system");
        ServentSingle.stop();
        System.exit(0);
    }
}
