package message;

import app.App;
import app.Config;
import app.Servent;

import java.util.Map;

public class WelcomeAskMessage extends Message {

    private static final long serialVersionUID = 1L;

    private Map<Integer, Integer> values;

    public WelcomeAskMessage(Servent receiver, Map<Integer, Integer> values) {
        super(Type.WELCOME_ASK, null, Config.LOCAL_SERVENT, receiver);

        this.values = values;
    }

    public WelcomeAskMessage(WelcomeAskMessage m) {
        super(m);

        values = m.values;
    }

    @Override
    protected Message copy() {
        return new WelcomeAskMessage(this);
    }

    @Override
    protected void handle(MessageHandler handler) {
        Config.CHORD.init(this);

        Servent bootstrap = new Servent(Config.BOOTSTRAP_PORT);
        App.send(new WelcomeTellMessage(bootstrap, Config.LOCAL_SERVENT.getPort()));

        App.send(new UpdateMessage(Config.CHORD.getNextServent()));
    }

    @Override
    public String toString() {
        return getType() + " with values " + getValues();
    }

    public Map<Integer, Integer> getValues() {
        return values;
    }
}
