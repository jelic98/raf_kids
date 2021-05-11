package message;

import app.Config;
import app.Servent;

public class WelcomeTellMessage extends Message {

    private static final long serialVersionUID = 1L;

    private int port;

    public WelcomeTellMessage(Servent receiver, int port) {
        super(Type.WELCOME_TELL, null, Config.LOCAL_SERVENT, receiver);

        this.port = port;
    }

    public WelcomeTellMessage(WelcomeTellMessage m) {
        super(m);

        port = m.port;
    }

    @Override
    protected Message copy() {
        return new WelcomeTellMessage(this);
    }

    @Override
    protected void handle(MessageHandler handler) {
        Config.BOOTSTRAP_SERVENTS.add(new Servent(getPort()));
    }

    @Override
    public String toString() {
        return getType() + " with port " + getPort();
    }

    public int getPort() {
        return port;
    }
}
