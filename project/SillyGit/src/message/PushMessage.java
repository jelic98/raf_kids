package message;

import app.Config;
import servent.Servent;
import data.Data;

public class PushMessage extends Message {

    private static final long serialVersionUID = 1L;

    private final Data data;

    public PushMessage(Servent receiver, Data data) {
        super(null, Config.LOCAL, receiver);

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
    protected void handle() {
        Config.SYSTEM.putValue(getData().getKey(), getData().getValue());
    }

    @Override
    public String toString() {
        return super.toString() + " with data " + getData();
    }

    public Data getData() {
        return data;
    }
}
