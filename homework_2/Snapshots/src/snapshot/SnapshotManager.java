package snapshot;

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
    }

    public void plus(int value) {
        balance.getAndAdd(-value);
    }

    public void minus(int value) {
        balance.getAndAdd(value);
    }

    public Snapshot getSnapshot() {
        return new Snapshot(balance.get(), plusHistory, minusHistory);
    }
}
