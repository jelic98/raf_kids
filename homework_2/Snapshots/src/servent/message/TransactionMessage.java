package servent.message;

import app.ServentInfo;
import app.snapshot_bitcake.BitcakeManager;
import app.snapshot_bitcake.LYBitcakeManager;

/**
 * Represents a bitcake transaction. We are sending some bitcakes to another node.
 *
 * @author bmilojkovic
 */
public class TransactionMessage extends BasicMessage {

    private static final long serialVersionUID = 1L;

    private final transient BitcakeManager bitcakeManager;

    public TransactionMessage(ServentInfo sender, ServentInfo receiver, int amount, BitcakeManager bitcakeManager) {
        super(MessageType.TRANSACTION, sender, receiver, String.valueOf(amount));
        this.bitcakeManager = bitcakeManager;
    }

    /**
     * We want to take away our amount exactly as we are sending, so our snapshots don't mess up.
     * This method is invoked by the sender just before sending.
     */
    @Override
    public void sendEffect() {
        int amount = Integer.parseInt(getMessageText());

        bitcakeManager.takeSomeBitcakes(amount);
        if (bitcakeManager instanceof LYBitcakeManager && isWhite()) {
            LYBitcakeManager lyFinancialManager = (LYBitcakeManager) bitcakeManager;

            lyFinancialManager.recordGiveTransaction(getReceiverInfo().getId(), amount);
        }
    }
}
