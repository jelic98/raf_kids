package message;

import app.Config;
import data.Key;
import servent.Servent;

public class RemoveMessage extends Message {

    private static final long serialVersionUID = 1L;

    private Key key;

    public RemoveMessage(Servent receiver, Key key) {
        super(null, Config.LOCAL, receiver);

        this.key = key;
    }

    public RemoveMessage(RemoveMessage m) {
        super(m);

        key = m.key;
    }

    @Override
    protected Message copy() {
        return new RemoveMessage(this);
    }

    @Override
    protected void handle() {
        Config.STORAGE.remove(getKey());
    }

    @Override
    public String toString() {
        return super.toString() + " with key " + getKey();
    }

    public Key getKey() {
        return key;
    }
}

