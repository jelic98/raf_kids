package message;

import app.Config;
import servent.Servent;

public class PingTellMessage extends Message {

    private static final long serialVersionUID = 1L;

    private final boolean check;

    public PingTellMessage(Servent receiver, boolean check) {
        super(null, Config.LOCAL, receiver);

        this.check = check;
    }

    public PingTellMessage(PingTellMessage m) {
        super(m);

        check = m.check;
    }

    @Override
    public boolean shouldPrint() {
        return false;
    }

    @Override
    protected Message copy() {
        return new PingTellMessage(this);
    }

    @Override
    protected void handle() {
        if (isCheck()) {
            Config.NETWORK.uncheck(getSender());
        } else {
            Config.NETWORK.pong(getSender());
        }
    }

    public boolean isCheck() {
        return check;
    }
}
