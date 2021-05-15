package message;

import app.App;
import app.Config;
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
        Servent[] servents = Config.NETWORK.getServents(getSender().getKey(), false);
        boolean joined = Config.NETWORK.addServent(getSender());
        App.send(new JoinTellMessage(getSender(), servents, joined));
    }
}

