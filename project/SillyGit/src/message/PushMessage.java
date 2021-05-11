package message;

import app.Config;
import app.Servent;

public class PushMessage extends Message {

    private static final long serialVersionUID = 1L;

    private int key;
    private int value;

    public PushMessage(Servent receiver, int key, int value) {
        super(Type.PUSH, null, Config.LOCAL_SERVENT, receiver);

        this.key = key;
        this.value = value;
    }

    public PushMessage(PushMessage m) {
        super(m);

        key = m.key;
        value = m.value;
    }

    @Override
    protected Message copy() {
        return new PushMessage(this);
    }

    @Override
    protected void handle(MessageHandler handler) {
        Config.CHORD.putValue(getKey(), getValue());
    }

    @Override
    public String toString() {
        return getType() + " " + getKey() + ":" + getValue();
    }

    public int getKey() {
        return key;
    }

    public int getValue() {
        return value;
    }
}
