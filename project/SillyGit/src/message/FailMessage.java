package message;

import app.App;
import app.Config;
import servent.Servent;

public class FailMessage extends Message {

    private static final long serialVersionUID = 1L;

    private final Servent servent;

    public FailMessage(Servent servent) {
        super(null, Config.LOCAL, Config.BOOTSTRAP);

        this.servent = servent;
    }

    public FailMessage(FailMessage m) {
        super(m);

        servent = m.servent;
    }

    @Override
    protected Message copy() {
        return new FailMessage(this);
    }

    @Override
    protected void handle() {
        if (Config.SYSTEM.removeServent(servent)) {
            App.print(String.format("Servent %s failed", getServent()));
        }
    }

    @Override
    public String toString() {
        return super.toString() + " with servent " + getServent();
    }

    public Servent getServent() {
        return servent;
    }
}

