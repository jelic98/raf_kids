package message;

import app.Servent;
import snapshot.Snapshot;

import java.util.Map;

public class TerminateMessage extends Message {

    private static final long serialVersionUID = 1L;

    private final Map<Servent, Snapshot> state;

    public TerminateMessage(Map<Servent, Snapshot> state) {
        super(Type.TERMINATE);

        this.state = state;
    }

    public TerminateMessage(TerminateMessage m) {
        super(m);

        state = m.state;
    }

    @Override
    protected Message copy() {
        return new TerminateMessage(this);
    }

    @Override
    public String toString() {
        return getType() + " with clock " + getClock();
    }

    public Map<Servent, Snapshot> getState() {
        return state;
    }
}
