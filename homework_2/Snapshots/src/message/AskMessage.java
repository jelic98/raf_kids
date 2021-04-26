package message;

public class AskMessage extends Message {

    private static final long serialVersionUID = 1L;

    public AskMessage() {
        super(Message.Type.ASK);
    }

    public AskMessage(AskMessage m) {
        super(m);
    }

    @Override
    protected Message copy() {
        return new AskMessage(this);
    }

    @Override
    public String toString() {
        return getType() + " with clock " + getClock();
    }
}
