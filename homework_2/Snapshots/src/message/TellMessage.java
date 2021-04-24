package message;

import app.Servent;
import snapshot.Snapshot;

public class TellMessage extends BroadcastMessage {

    private static final long serialVersionUID = 1L;

    private final Snapshot snapshot;

    public TellMessage(Servent sender, Servent receiver, Snapshot snapshot) {
        super(Message.Type.TELL, null, sender, receiver);

        this.snapshot = snapshot;
    }

    public TellMessage(TellMessage m) {
        super(m);

        snapshot = m.snapshot;
    }

    @Override
    protected Message clone() {
        return new TellMessage(this);
    }

    @Override
    public String toString() {
        return getType() + " " + snapshot.getBalance();
    }

    public Snapshot getSnapshot() {
        return snapshot;
    }
}
