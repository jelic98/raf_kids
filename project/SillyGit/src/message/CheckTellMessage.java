package message;

import app.Config;
import servent.Servent;

public class CheckTellMessage extends Message {

    private static final long serialVersionUID = 1L;

    private final Servent servent;
    private final boolean active;

    public CheckTellMessage(Servent receiver, Servent servent, boolean active) {
        super(null, Config.LOCAL, receiver);

        this.servent = servent;
        this.active = active;
    }

    public CheckTellMessage(CheckTellMessage m) {
        super(m);

        servent = m.servent;
        active = m.active;
    }

    @Override
    public boolean shouldPrint() {
        return false;
    }

    @Override
    protected Message copy() {
        return new CheckTellMessage(this);
    }

    @Override
    protected void handle() {
        if (isActive()) {
            Config.SYSTEM.pong(getServent());
        }
    }

    @Override
    public String toString() {
        return super.toString() + " with servent " + getServent();
    }

    public Servent getServent() {
        return servent;
    }

    public boolean isActive() {
        return active;
    }
}

