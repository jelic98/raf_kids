package message;

import app.Config;
import servent.Servent;

public class PingTellMessage extends Message {

    private static final long serialVersionUID = 1L;

    private boolean check;

    public PingTellMessage(Servent receiver, boolean check) {
        super(null, Config.LOCAL, receiver);

        this.check = check;
    }

    public PingTellMessage(PingTellMessage m) {
        super(m);

        check = m.check;
    }

    @Override
    protected Message copy() {
        return new PingTellMessage(this);
    }

    @Override
    protected void handle() {
        if (isCheck()) {
            Config.SYSTEM.uncheck(getSender());
        } else {
            Config.SYSTEM.pong(getSender());
        }
    }

    public boolean isCheck() {
        return check;
    }
}
