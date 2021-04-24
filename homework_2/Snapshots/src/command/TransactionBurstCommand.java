package command;

import app.App;
import app.Config;
import app.Servent;
import app.ServentState;
import message.TransactionMessage;
import snapshot.SnapshotManager;

public class TransactionBurstCommand implements Command {

    private static final int TRANSACTION_COUNT = 5;

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
        App.print(String.format("Bursting %d transactions", TRANSACTION_COUNT));

        new Thread(() -> {
            for (int i = 0; i < TRANSACTION_COUNT; i++) {
                for (Servent neighbor : Config.LOCAL_SERVENT.getNeighbors()) {
                    TransactionMessage message = new TransactionMessage(i + 1, Config.LOCAL_SERVENT, snapshotManager);
                    ServentState.commitMessage(message, true);
                    App.send(message.setReceiver(neighbor));
                }
            }
        }).start();
    }
}