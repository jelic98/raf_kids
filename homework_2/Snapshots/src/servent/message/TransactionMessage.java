package servent.message;

import app.AppConfig;
import app.Servent;
import servent.snapshot.SnapshotManager;

public class TransactionMessage extends CausalBroadcastMessage {

    private static final long serialVersionUID = 1L;

    private final transient SnapshotManager snapshotManager;

    public TransactionMessage(int amount, Servent receiver, SnapshotManager snapshotManager) {
        super(MessageType.TRANSACTION, String.valueOf(amount), AppConfig.LOCAL_SERVENT, receiver);

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
    public void sendEffect() {
        snapshotManager.minus(Integer.parseInt(getText()));
    }
}



