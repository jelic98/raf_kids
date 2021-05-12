package message;

import app.App;
import app.Config;
import servent.Servent;

public class HailTellMessage extends Message {

    private static final long serialVersionUID = 1L;

    private final Servent servent;

    public HailTellMessage(Servent receiver, Servent servent) {
        super(null, Config.LOCAL, receiver);

        this.servent = servent;
    }

    public HailTellMessage(HailTellMessage m) {
        super(m);

        servent = m.servent;
    }

    @Override
    protected Message copy() {
        return new HailTellMessage(this);
    }

    @Override
    protected void handle() {
        Servent servent = getServent();

        if (servent == null) {
            App.send(new PublishMessage());
        } else {
            App.send(new RegisterAskMessage(servent));
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

