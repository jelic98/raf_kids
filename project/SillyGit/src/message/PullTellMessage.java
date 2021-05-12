package message;

import app.App;
import app.Config;
import servent.Servent;
import data.Data;
import data.Key;
import data.Value;

public class PullTellMessage extends Message {

    private static final long serialVersionUID = 1L;

    private final Data data;

    public PullTellMessage(Servent receiver, Data data) {
        super(null, Config.LOCAL, receiver);

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
    protected void handle() {
        Key key = getData().getKey();
        Value value = getData().getValue();

        if (value == null) {
            App.print("Unknown key: " + key);
        } else {
            App.print("Pulled: " + key + "->" + value);
        }
    }

    @Override
    public String toString() {
        return super.toString() + " with data " + getData();
    }

    public Data getData() {
        return data;
    }
}
