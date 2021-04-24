package servent.handler;

import servent.message.TransactionMessage;
import servent.snapshot.SnapshotManager;

public class TransactionHandler implements Runnable {

    private final TransactionMessage message;
    private final SnapshotManager snapshotManager;

    public TransactionHandler(TransactionMessage message, SnapshotManager snapshotManager) {
        this.message = message;
        this.snapshotManager = snapshotManager;
    }

    @Override
    public void run() {
        snapshotManager.plus(Integer.parseInt(message.getText()));
    }
}