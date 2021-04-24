package command;

import app.App;
import app.Config;
import app.Servent;
import message.TransactionMessage;
import snapshot.SnapshotManager;

public class TransactionBurstCommand implements Command {

    private static final int TRANSACTION_COUNT = 5;
    private static final int BURST_WORKERS = 5;
    private static final int MAX_TRANSFER_AMOUNT = 10;

    private final SnapshotManager snapshotManager;

    public TransactionBurstCommand(SnapshotManager snapshotManager) {
        this.snapshotManager = snapshotManager;
    }

    @Override
    public String getName() {
        return "transaction_burst";
    }

    @Override
    public void execute(String args) {
        App.print(String.format("Bursting %d transactions", BURST_WORKERS * TRANSACTION_COUNT));

        for (int i = 0; i < BURST_WORKERS; i++) {
            new Thread(() -> {
                for (int i1 = 0; i1 < TRANSACTION_COUNT; i1++) {
                    for (Servent neighbor : Config.LOCAL_SERVENT.getNeighbors()) {
                        int amount = 1 + (int) (Math.random() * MAX_TRANSFER_AMOUNT);
                        App.send(new TransactionMessage(amount, neighbor, snapshotManager));
                    }
                }
            }).start();
        }
    }
}