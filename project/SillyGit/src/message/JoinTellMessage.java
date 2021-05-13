package message;

import app.App;
import app.Config;
import servent.Servent;

import java.util.Arrays;

public class JoinTellMessage extends Message {

    private static final long serialVersionUID = 1L;

    private final Servent[] servents;

    public JoinTellMessage(Servent receiver, Servent[] servents) {
        super(null, Config.LOCAL, receiver);

        this.servents = servents;
    }

    public JoinTellMessage(JoinTellMessage m) {
        super(m);

        servents = m.servents;
    }

    @Override
    protected Message copy() {
        return new JoinTellMessage(this);
    }

    @Override
    protected void handle() {
        Servent[] servents = getServents();

        if (servents != null) {
            for (Servent servent : servents) {
                App.send(new PingAskMessage(servent));
            }
        }
    }

    @Override
    public String toString() {
        return super.toString() + " with servents " + Arrays.toString(getServents());
    }

    public Servent[] getServents() {
        return servents;
    }
}

