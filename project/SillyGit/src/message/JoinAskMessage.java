package message;

import app.App;
import app.Config;
import data.Key;
import servent.Servent;

public class JoinAskMessage extends Message {

    private static final long serialVersionUID = 1L;

    public JoinAskMessage() {
        super(null, Config.LOCAL, Config.BOOTSTRAP);
    }

    public JoinAskMessage(JoinAskMessage m) {
        super(m);
    }

    @Override
    protected Message copy() {
        return new JoinAskMessage(this);
    }

    @Override
    protected void handle() {
        Key key = new Key(getSender().hashCode());
        Servent[] servents = Config.SYSTEM.getServents(key);
        Config.SYSTEM.addServent(getSender());
        App.send(new JoinTellMessage(getSender(), servents));
    }
}

