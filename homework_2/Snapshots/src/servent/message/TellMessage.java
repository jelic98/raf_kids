package servent.message;

import app.Servent;
import servent.snapshot.Snapshot;

public class TellMessage extends BroadcastMessage {

    private static final long serialVersionUID = 1L;

    private final Snapshot snapshot;

    public TellMessage(Servent sender, Servent receiver, Snapshot snapshot) {
        super(MessageType.TELL, null, sender, receiver);

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

    public Snapshot getSnapshot() {
        return snapshot;
    }
}
