package message;

import app.App;
import app.Config;
import app.Servent;
import app.ServentSingle;

public class SorryMessage extends Message {

    private static final long serialVersionUID = 1L;

    public SorryMessage(Servent receiver) {
        super(null, Config.LOCAL_SERVENT, receiver);
    }

    public SorryMessage(SorryMessage m) {
        super(m);
    }

    @Override
    protected Message copy() {
        return new SorryMessage(this);
    }

    @Override
    protected void handle(MessageHandler handler) {
        App.error("Cannot enter system");
        ServentSingle.stop();
    }
}
