package message;

import app.App;
import app.Config;
import data.Key;
import file.FileData;
import servent.Servent;

public class PullAskMessage extends Message {

    private static final long serialVersionUID = 1L;

    private Key key;

    public PullAskMessage(Servent receiver, Key key) {
        super(null, Config.LOCAL, receiver);

        this.key = key;
    }

    public PullAskMessage(PullAskMessage m) {
        super(m);

        key = m.key;
    }

    @Override
    protected Message copy() {
        return new PullAskMessage(this);
    }

    @Override
    protected void handle() {
        FileData data = Config.STORAGE.get(getKey());

        if (data != null) {
            App.send(new PullTellMessage(getSender(), data));
        }
    }

    @Override
    public String toString() {
        return super.toString() + " with key " + getKey();
    }

    public Key getKey() {
        return key;
    }
}

