package message;

public class StopMessage extends Message {

    private static final long serialVersionUID = 1L;

    public StopMessage() {
        super(Type.STOP);
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
