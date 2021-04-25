package snapshot;

import app.Config;
import app.Servent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class SnapshotManager {

    private final AtomicInteger balance = new AtomicInteger(1000);
    private final Map<Servent, Integer> plusHistory;
    private final Map<Servent, Integer> minusHistory;

    public SnapshotManager() {
        plusHistory = new ConcurrentHashMap<>();
        minusHistory = new ConcurrentHashMap<>();

        for (Servent neighbor : Config.SERVENTS) {
            plusHistory.put(neighbor, 0);
            minusHistory.put(neighbor, 0);
        }
    }

    public void plus(Servent servent, int value) {
        balance.getAndAdd(value);
        plusHistory.compute(servent, (k, v) -> v + value);
    }

    public void minus(Servent servent, int value) {
        balance.getAndAdd(-value);
        minusHistory.compute(servent, (k, v) -> v + value);
    }

    public Snapshot getSnapshot() {
        return new Snapshot(balance.get(), plusHistory, minusHistory);
    }
}
