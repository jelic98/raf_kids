package message;

public class StopMessage extends BroadcastMessage {

    private static final long serialVersionUID = 1L;

    public StopMessage() {
        super(Type.STOP, null);
    }

    public StopMessage(StopMessage m) {
        super(m);
    }

    @Override
    protected Message copy() {
        return new StopMessage(this);
    }

    @Override
    public String toString() {
        return getType() + " with clock " + getClock();
    }
}
