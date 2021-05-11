package message;

import app.App;
import app.Config;
import app.Servent;

public class HailTellMessage extends Message {

    private static final long serialVersionUID = 1L;

    private Servent servent;

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
            App.error("Error in contacting bootstrap");
            System.exit(0);
        } else if (servent.getPort() == -1) {
            App.error("First node in Chord system");
        } else {
            App.send(new RegisterMessage(servent));
        }
    }

    @Override
    public String toString() {
        return getType() + " " + getServent();
    }

    public Servent getServent() {
        return servent;
    }
}

