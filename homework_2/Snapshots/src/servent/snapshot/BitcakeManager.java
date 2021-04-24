package servent.snapshot;

public interface BitcakeManager {

    void takeBitcakes(int amount);

    void addBitcakes(int amount);

    int getCurrentBitcakeAmount();
}