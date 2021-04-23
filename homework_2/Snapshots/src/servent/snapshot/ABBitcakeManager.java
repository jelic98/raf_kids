package servent.snapshot;

import java.util.concurrent.atomic.AtomicInteger;

public class ABBitcakeManager implements BitcakeManager {

    private final AtomicInteger currentAmount = new AtomicInteger(1000);

    @Override
    public void takeBitcakes(int amount) {
        currentAmount.getAndAdd(-amount);
    }

    @Override
    public void addBitcakes(int amount) {
        currentAmount.getAndAdd(amount);
    }

    @Override
    public int getCurrentBitcakeAmount() {
        return currentAmount.get();
    }
}
