package cli.command;

import app.AppConfig;
import app.ServentInfo;
import servent.snapshot.BitcakeManager;
import servent.message.Message;
import servent.message.TransactionMessage;
import servent.message.MessageUtil;

public class TransactionBurstCommand implements Command {

    private static final int TRANSACTION_COUNT = 5;
    private static final int BURST_WORKERS = 10;
    private static final int MAX_TRANSFER_AMOUNT = 10;

    private BitcakeManager bitcakeManager;

    public TransactionBurstCommand(BitcakeManager bitcakeManager) {
        this.bitcakeManager = bitcakeManager;
    }

    private class TransactionBurstWorker implements Runnable {

        @Override
        public void run() {
            for (int i = 0; i < TRANSACTION_COUNT; i++) {
                for (int neighbor : AppConfig.myServentInfo.getNeighbors()) {
                    int amount = 1 + (int) (Math.random() * MAX_TRANSFER_AMOUNT);
                    MessageUtil.sendMessage(new TransactionMessage(AppConfig.myServentInfo,
                            AppConfig.getInfoById(neighbor), amount, bitcakeManager));
                }
            }
        }
    }

    @Override
    public String getName() {
        return "transaction_burst";
    }

    @Override
    public void execute(String args) {
        for (int i = 0; i < BURST_WORKERS; i++) {
            Thread t = new Thread(new TransactionBurstWorker());

            t.start();
        }
    }
}