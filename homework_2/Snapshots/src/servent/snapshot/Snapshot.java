package servent.snapshot;

import app.Servent;
import java.io.Serializable;
import java.util.Map;

public class Snapshot implements Serializable {

    private static final long serialVersionUID = 1L;

    private final int balance;
    private final Map<Servent, Integer> plusHistory;
    private final Map<Servent, Integer> minusHistory;

    public Snapshot(int balance, Map<Servent, Integer> plusHistory, Map<Servent, Integer> minusHistory) {
        this.balance = balance;
        this.plusHistory = plusHistory;
        this.minusHistory = minusHistory;
    }

    public int getBalance() {
        return balance;
    }

    public Map<Servent, Integer> getPlusHistory() {
        return plusHistory;
    }

    public Map<Servent, Integer> getMinusHistory() {
        return minusHistory;
    }
}
