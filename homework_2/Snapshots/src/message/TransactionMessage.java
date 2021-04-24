package message;

import app.Config;
import app.Servent;
import snapshot.SnapshotManager;

public class TransactionMessage extends BroadcastMessage {

    private static final long serialVersionUID = 1L;

    private final transient SnapshotManager snapshotManager;

    public TransactionMessage(int amount, Servent receiver, SnapshotManager snapshotManager) {
        super(Message.Type.TRANSACTION, String.valueOf(amount), Config.LOCAL_SERVENT, receiver);

        this.snapshotManager = snapshotManager;
    }

    public TransactionMessage(TransactionMessage m) {
        super(m);

        snapshotManager = m.snapshotManager;
    }

    @Override
    protected Message clone() {
        return new TransactionMessage(this);
    }

    @Override
    public String toString() {
        return getType() + " " + getText();
    }

    @Override
    public void sendEffect() {
        snapshotManager.minus(Integer.parseInt(getText()));
    }
}



