package message;

import app.Servent;
import snapshot.Snapshot;

public class TellMessage extends Message {

    private static final long serialVersionUID = 1L;

    private final Snapshot snapshot;
    private final Servent destination;

    public TellMessage(Servent sender, Servent receiver, Snapshot snapshot, Servent destination) {
        super(Message.Type.TELL, null, sender, receiver);

        this.snapshot = snapshot;
        this.destination = destination;
    }

    public TellMessage(TellMessage m) {
        super(m);

        snapshot = m.snapshot;
        destination = m.destination;
    }

    @Override
    protected Message clone() {
        return new TellMessage(this);
    }

    @Override
    public String toString() {
        return getType() + " of " + snapshot.getBalance() + " bitcakes with clock " + getClock();
    }

    public Snapshot getSnapshot() {
        return snapshot;
    }

    public Servent getDestination() {
        return destination;
    }
}
