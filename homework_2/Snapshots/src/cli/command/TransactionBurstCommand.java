package cli.command;

import app.AppConfig;
import app.Servent;
import servent.message.MessageUtil;
import servent.message.TransactionMessage;
import servent.snapshot.BitcakeManager;
import servent.snapshot.CausalBroadcastShared;

public class TransactionBurstCommand implements Command {

    private static final int TRANSACTION_COUNT = 5;
    private static final int BURST_WORKERS = 5;
    private static final int MAX_TRANSFER_AMOUNT = 10;

    private BitcakeManager bitcakeManager;

    public TransactionBurstCommand(BitcakeManager bitcakeManager) {
        this.bitcakeManager = bitcakeManager;
    }

    @Override
    public String getName() {
        return "transaction_burst";
    }

    @Override
    public void execute(String args) {
        AppConfig.print(String.format("Bursting %d transactions", BURST_WORKERS * TRANSACTION_COUNT));

        for (int i = 0; i < BURST_WORKERS; i++) {
            new Thread(() -> {
                for (int i1 = 0; i1 < TRANSACTION_COUNT; i1++) {
                    for (Servent neighbor : AppConfig.LOCAL_SERVENT.getNeighbors()) {
                        int amount = 1 + (int) (Math.random() * MAX_TRANSFER_AMOUNT);
                        MessageUtil.sendMessage(new TransactionMessage(AppConfig.LOCAL_SERVENT, neighbor, amount, bitcakeManager, CausalBroadcastShared.getClockReceived()));
                    }
                }
            }).start();
        }
    }
}