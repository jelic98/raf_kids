package message;

import app.App;
import app.Config;
import app.Servent;
import data.Data;

public class PullTellMessage extends Message {

    private static final long serialVersionUID = 1L;

    private final Data data;

    public PullTellMessage(Servent receiver, Data data) {
        super(Type.PULL_TELL, null, Config.LOCAL_SERVENT, receiver);

        this.data = data;
    }

    public PullTellMessage(PullTellMessage m) {
        super(m);

        data = m.data;
    }

    @Override
    protected Message copy() {
        return new PullTellMessage(this);
    }

    @Override
    protected void handle(MessageHandler handler) {
        if (getData().getValue() != null) {
            App.print("No such key: " + getData().getKey());
        }
    }

    @Override
    public String toString() {
        return getType() + " with data " + getData();
    }

    public Data getData() {
        return data;
    }
}
