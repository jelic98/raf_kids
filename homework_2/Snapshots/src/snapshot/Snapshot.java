package snapshot;

import app.Servent;
import message.BroadcastMessage;
import message.Message;
import message.TransactionMessage;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Snapshot implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final int STARTING_BALANCE = 1000;

    private final Servent servent;
    private final Map<Servent, Integer> plusHistory;
    private final Map<Servent, Integer> minusHistory;
    private final List<BroadcastMessage> messageHistory;

    public Snapshot(Servent servent, Map<Servent, Integer> plusHistory, Map<Servent, Integer> minusHistory) {
        this.servent = servent;
        this.plusHistory = new ConcurrentHashMap<>(plusHistory);
        this.minusHistory = new ConcurrentHashMap<>(minusHistory);

        messageHistory = null;
    }

    public Snapshot(Servent servent, List<BroadcastMessage> messageHistory) {
        this.servent = servent;
        this.messageHistory = messageHistory;

        plusHistory = null;
        minusHistory = null;
    }

    public int getBalance() {
        int balance = STARTING_BALANCE;

        if (messageHistory != null) {
            for (BroadcastMessage message : messageHistory) {
                if (message.getType() == Message.Type.TRANSACTION) {
                    TransactionMessage transaction = (TransactionMessage) message;

                    if (transaction.getDestination().equals(servent)) {
                        balance += transaction.getAmount();
                    } else if (transaction.getSender().equals(servent)) {
                        balance -= transaction.getAmount();
                    }
                }
            }
        } else if (plusHistory != null && minusHistory != null) {
            for (Map.Entry<Servent, Integer> e : plusHistory.entrySet()) {
                balance += e.getValue();
            }

            for (Map.Entry<Servent, Integer> e : minusHistory.entrySet()) {
                balance -= e.getValue();
            }
        }

        return balance;
    }

    public Map<Servent, Integer> getPlusHistory() {
        return plusHistory;
    }

    public Map<Servent, Integer> getMinusHistory() {
        return minusHistory;
    }

    public List<BroadcastMessage> getMessageHistory() {
        return messageHistory;
    }
}
