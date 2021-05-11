package message;

import app.App;
import app.Config;
import app.Servent;

import java.util.ArrayList;
import java.util.Random;

public class HailAskMessage extends Message {

    private static final long serialVersionUID = 1L;

    private int port;

    public HailAskMessage(Servent receiver, int port) {
        super(Type.HAIL_ASK, null, Config.LOCAL_SERVENT, receiver);

        this.port = port;
    }

    public HailAskMessage(HailAskMessage m) {
        super(m);

        port = m.port;
    }

    @Override
    protected Message copy() {
        return new HailAskMessage(this);
    }

    @Override
    protected void handle(MessageHandler handler) {
        Servent servent = null;

        if (Config.BOOTSTRAP_SERVENTS.isEmpty()) {
            Config.BOOTSTRAP_SERVENTS.add(new Servent(getPort()));
        } else {
            Random random = new Random(System.currentTimeMillis());
            int index = random.nextInt(Config.BOOTSTRAP_SERVENTS.size());
            servent = Config.BOOTSTRAP_SERVENTS.get(index);
        }

        App.send(new HailTellMessage(getSender(), servent));
    }

    @Override
    public String toString() {
        return getType() + " with port " + getPort();
    }

    public int getPort() {
        return port;
    }
}

