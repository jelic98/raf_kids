package message;

import app.App;
import app.Config;
import app.Servent;
import data.Data;
import data.Key;
import data.Value;

import java.util.Map;

public class PullAskMessage extends Message {

    private static final long serialVersionUID = 1L;

    private final Key key;

    public PullAskMessage(Servent receiver, Key key) {
        super(Type.PULL_ASK, null, Config.LOCAL_SERVENT, receiver);

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
    protected void handle(MessageHandler handler) {
        Key key = getKey();

        if (Config.CHORD.containsKey(key)) {
            Map<Key, Value> chunk = Config.CHORD.getChunk();
            Value value = null;

            if (chunk.containsKey(key)) {
                value = chunk.get(key);
            }

            App.send(new PullTellMessage(getSender(), new Data(key, value)));
        } else {
            App.send(setReceiver(Config.CHORD.getServent(key)));
        }
    }

    @Override
    public String toString() {
        return getType() + " with key " + getKey();
    }

    public Key getKey() {
        return key;
    }
}
