package message;

import app.Config;
import app.Servent;
import app.ServentState;

public class TransactionMessage extends BroadcastMessage {

    private static final long serialVersionUID = 1L;

    private final int amount;
    private final Servent destination;

    public TransactionMessage(int amount, Servent destination) {
        super(Message.Type.TRANSACTION, null, Config.LOCAL_SERVENT, Config.LOCAL_SERVENT);

        this.amount = amount;
        this.destination = destination;
    }

    public TransactionMessage(TransactionMessage m) {
        super(m);

        amount = m.amount;
        destination = m.destination;
    }

    @Override
    protected Message clone() {
        return new TransactionMessage(this);
    }

    @Override
    public String toString() {
        return getType() + " of " + amount + " bitcakes to " + destination + " with clock " + getClock();
    }

    public int getAmount() {
        return amount;
    }

    public Servent getDestination() {
        return destination;
    }
}



