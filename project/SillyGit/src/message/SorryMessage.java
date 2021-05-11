package message;

import app.App;
import app.Config;
import app.Servent;

public class SorryMessage extends Message {

    private static final long serialVersionUID = 1L;

    public SorryMessage(Servent receiver) {
        super(Type.SORRY, null, Config.LOCAL_SERVENT, receiver);
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
        System.exit(0);
    }

    @Override
    public String toString() {
        return String.valueOf(getType());
    }
}
