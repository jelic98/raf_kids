package message;

import app.App;
import app.Config;
import app.Servent;

import java.util.Map;

public class PullAskMessage extends Message {

    private static final long serialVersionUID = 1L;

    private int key;

    public PullAskMessage(Servent receiver, int key) {
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
        int key = getKey();

        if (Config.CHORD.isKeyMine(key)) {
            Map<Integer, Integer> valueMap = Config.CHORD.getValueMap();
            int value = -1;

            if (valueMap.containsKey(key)) {
                value = valueMap.get(key);
            }

            App.send(new PullTellMessage(getSender(), key, value));
        } else {
            Servent nextNode = Config.CHORD.getServent(key);
            App.send(new PullAskMessage(nextNode, getKey()));
        }
    }

    @Override
    public String toString() {
        return getType() + " " + getKey();
    }

    public int getKey() {
        return key;
    }
}
