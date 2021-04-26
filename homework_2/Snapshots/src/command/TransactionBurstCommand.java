package command;

import app.App;
import app.Config;
import app.Servent;
import app.ServentState;
import message.MessageHandler;
import message.TransactionMessage;

public class TransactionBurstCommand implements Command {

    private static final int TRANSACTION_COUNT = 2;
    private static final int TRANSACTION_AMOUNT = 10;

    @Override
    public String getName() {
        return "transaction_burst";
    }

    @Override
    public void execute(String args) {
        App.print(String.format("Bursting %d transactions", TRANSACTION_COUNT));

        new Thread(() -> {
            for (int i = 0; i < TRANSACTION_COUNT; i++) {
                for (Servent destination : Config.SERVENTS) {
                    if (destination.equals(Config.LOCAL_SERVENT)) {
                        continue;
                    }

                    ServentState.getSnapshotManager().minus(TRANSACTION_AMOUNT, destination);
                    MessageHandler.handle(new TransactionMessage(TRANSACTION_AMOUNT, destination));
                }
            }
        }).start();
    }
}