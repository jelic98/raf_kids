package message;

public class TerminateMessage extends Message {

    private static final long serialVersionUID = 1L;

    public TerminateMessage() {
        super(Type.TERMINATE);
    }

    public TerminateMessage(TerminateMessage m) {
        super(m);
    }

    @Override
    protected Message clone() {
        return new TerminateMessage(this);
    }

    @Override
    public String toString() {
        return getType() + " with clock " + getClock();
    }
}
