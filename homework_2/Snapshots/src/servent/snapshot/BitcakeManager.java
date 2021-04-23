package servent.snapshot;

/**
 * Describes a bitcake manager. These classes will have the methods
 * for handling snapshot recording and sending info to a collector.
 *
 * @author bmilojkovic
 *
 */
public interface BitcakeManager {

    void takeBitcakes(int amount);
    void addBitcakes(int amount);
    int getCurrentBitcakeAmount();
}