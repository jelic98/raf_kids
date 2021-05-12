package message;

import app.App;
import app.Config;
import data.Data;
import data.Key;
import data.Value;

public class PullAskMessage extends Message {

    private static final long serialVersionUID = 1L;

    private final Key key;

    public PullAskMessage(Key key) {
        super(null, Config.LOCAL, Config.SYSTEM.getServent(key));

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
        Key key = getKey();

        if (Config.SYSTEM.containsKey(key)) {
            Value value = Config.SYSTEM.getValue(key);

            App.send(new PullTellMessage(getSender(), new Data(key, value)));
        } else {
            App.send(redirect(Config.SYSTEM.getServent(key)));
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
