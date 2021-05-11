package message;

import app.Config;
import app.Servent;
import data.Data;

public class PushMessage extends Message {

    private static final long serialVersionUID = 1L;

    private final Data data;

    public PushMessage(Servent receiver, Data data) {
        super(Type.PUSH, null, Config.LOCAL_SERVENT, receiver);

        this.data = data;
    }

    public PushMessage(PushMessage m) {
        super(m);

        data = m.data;
    }

    @Override
    protected Message copy() {
        return new PushMessage(this);
    }

    @Override
    protected void handle(MessageHandler handler) {
        Config.CHORD.putValue(getData().getKey(), getData().getValue());
    }

    @Override
    public String toString() {
        return getType() + " with data " + getData();
    }

    public Data getData() {
        return data;
    }
}
