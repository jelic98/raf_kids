package message;

import app.Config;
import servent.Servent;

public class PushTellMessage extends Message {

    private static final long serialVersionUID = 1L;

    private boolean conflict;

    public PushTellMessage(Servent receiver, boolean conflict) {
        super(null, Config.LOCAL, receiver);

        this.conflict = conflict;
    }

    public PushTellMessage(PushTellMessage m) {
        super(m);

        conflict = m.conflict;
    }

    @Override
    protected Message copy() {
        return new PushTellMessage(this);
    }

    @Override
    protected void handle() {
        if (isConflict()) {

        }else {

        }
    }

    @Override
    public String toString() {
        return super.toString() + " with conflict " + isConflict();
    }

    public boolean isConflict() {
        return conflict;
    }
}

