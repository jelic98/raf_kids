package message;

import app.App;
import app.Config;
import app.Servent;

public class PullTellMessage extends Message {

    private static final long serialVersionUID = 1L;

    private int key;
    private int value;

    public PullTellMessage(Servent receiver, int key, int value) {
        super(Type.PULL_TELL, null, Config.LOCAL_SERVENT, receiver);

        this.key = key;
        this.value = value;
    }

    public PullTellMessage(PullTellMessage m) {
        super(m);

        key = m.key;
        value = m.value;
    }

    @Override
    protected Message copy() {
        return new PullTellMessage(this);
    }

    @Override
    protected void handle(MessageHandler handler) {
        if (getValue() == -1) {
            App.print("No such key: " + getKey());
        }
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
