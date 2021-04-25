package snapshot;

import app.Servent;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Snapshot implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final int STARTING_BALANCE = 1000;

    private final Map<Servent, Integer> plusHistory;
    private final Map<Servent, Integer> minusHistory;

    public Snapshot(Map<Servent, Integer> plusHistory, Map<Servent, Integer> minusHistory) {
        this.plusHistory = new ConcurrentHashMap<>(plusHistory);
        this.minusHistory = new ConcurrentHashMap<>(minusHistory);
    }

    public int getBalance() {
        int balance = STARTING_BALANCE;

        for (Map.Entry<Servent, Integer> e : plusHistory.entrySet()) {
            balance += e.getValue();
        }

        for (Map.Entry<Servent, Integer> e : minusHistory.entrySet()) {
            balance -= e.getValue();
        }

        return balance;
    }

    public Map<Servent, Integer> getPlusHistory() {
        return plusHistory;
    }

    public Map<Servent, Integer> getMinusHistory() {
        return minusHistory;
    }
}
