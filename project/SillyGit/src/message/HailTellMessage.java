package message;

import app.App;
import app.Config;
import app.Servent;

public class HailTellMessage extends Message {

    private static final long serialVersionUID = 1L;

    private final Servent servent;

    public HailTellMessage(Servent receiver, Servent servent) {
        super(Type.HAIL_TELL, null, Config.LOCAL_SERVENT, receiver);

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
    protected void handle(MessageHandler handler) {
        Servent servent = getServent();

        if (servent == null) {
            Config.CHORD.initialize(Config.LOCAL_SERVENT, null);

            App.send(new PublishMessage());
        } else {
            App.send(new RegisterAskMessage(servent));
        }
    }

    @Override
    public String toString() {
        return getType() + " with servent " + getServent();
    }

    public Servent getServent() {
        return servent;
    }
}

