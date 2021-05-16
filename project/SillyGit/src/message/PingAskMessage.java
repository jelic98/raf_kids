package message;

import app.App;
import app.Config;
import servent.Servent;

public class PingAskMessage extends Message {

    private static final long serialVersionUID = 1L;

    private final boolean check;

    public PingAskMessage(Servent receiver, boolean check) {
        super(null, Config.LOCAL, receiver);

        this.check = check;
    }

    public PingAskMessage(PingAskMessage m) {
        super(m);

        check = m.check;
    }

    @Override
    public boolean shouldPrint() {
        return false;
    }

    @Override
    protected Message copy() {
        return new PingAskMessage(this);
    }

    @Override
    protected void handle() {
        Config.NETWORK.addServent(getSender());
        App.send(new PingTellMessage(getSender(), isCheck()));
    }

    public boolean isCheck() {
        return check;
    }
}
