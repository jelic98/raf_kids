package message;

import app.App;
import app.Config;
import servent.Servent;

public class CheckAskMessage extends Message {

    private static final long serialVersionUID = 1L;

    private final Servent servent;

    public CheckAskMessage(Servent receiver, Servent servent) {
        super(null, Config.LOCAL, receiver);

        this.servent = servent;
    }

    public CheckAskMessage(CheckAskMessage m) {
        super(m);

        servent = m.servent;
    }

    @Override
    public boolean shouldPrint() {
        return false;
    }

    @Override
    protected Message copy() {
        return new CheckAskMessage(this);
    }

    @Override
    protected void handle() {
        Config.NETWORK.check(getServent());

        boolean active = Config.NETWORK.isChecked(getServent());

        if (!active) {
            Config.NETWORK.removeServent(getServent());
        }

        App.send(new CheckTellMessage(getServent(), getServent(), active));
    }

    @Override
    public String toString() {
        return super.toString() + " with servent " + getServent();
    }

    public Servent getServent() {
        return servent;
    }
}

