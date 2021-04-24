package servent.snapshot;

import app.AppConfig;
import app.Servent;
import servent.message.AskMessage;
import servent.message.CausalBroadcastMessage;
import servent.message.MessageUtil;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class SnapshotCollector implements Runnable {

    private volatile boolean working = true;
    private AtomicBoolean collecting = new AtomicBoolean(false);
    private Map<Servent, Integer> results = new ConcurrentHashMap<>();
    private BitcakeManager bitcakeManager;

    public SnapshotCollector() {
        switch (AppConfig.SNAPSHOT_TYPE) {
            case AB:
                bitcakeManager = new ABBitcakeManager();
                break;
            case AV:
                bitcakeManager = new AVBitcakeManager();
                break;
        }
    }

    public BitcakeManager getBitcakeManager() {
        return bitcakeManager;
    }

    @Override
    public void run() {
        while (working) {
            if (!collecting.get()) {
                continue;
            }

            AppConfig.print("Sending ASK messages");

            switch (AppConfig.SNAPSHOT_TYPE) {
                case AB:
                    CausalBroadcastShared.setAskSender(AppConfig.LOCAL_SERVENT);

                    CausalBroadcastMessage message = new AskMessage(AppConfig.LOCAL_SERVENT, null, CausalBroadcastShared.getClockReceived());

                    for (Servent neighbor : AppConfig.LOCAL_SERVENT.getNeighbors()) {
                        AppConfig.print("Sending ASK to servent " + neighbor);
                        MessageUtil.sendMessage(message.setReceiver(neighbor));
                    }

                    CausalBroadcastShared.commitMessage((CausalBroadcastMessage) message.setReceiver(AppConfig.LOCAL_SERVENT), true);

                    addSnapshot(AppConfig.LOCAL_SERVENT, bitcakeManager.getCurrentBitcakeAmount());

                    break;
                case AV:
                    break;
            }

            AppConfig.print("Receiving TELL messages");

            while (results.size() < AppConfig.SERVENT_COUNT) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                if (!working) {
                    return;
                }
            }

            AppConfig.print("Gathering results");

            switch (AppConfig.SNAPSHOT_TYPE) {
                case AB:
                    int sum = 0;

                    for (Entry<Servent, Integer> e : results.entrySet()) {
                        sum += e.getValue();
                        AppConfig.print(String.format("Servent %s has %d bitcakes", e.getKey(), e.getValue()));
                    }

                    AppConfig.print("Total bitcakes: " + sum);

                    results.clear();
                    collecting.set(false);

                    break;
                case AV:
                    break;
            }
        }
    }

    public void addSnapshot(Servent servent, int amount) {
        results.put(servent, amount);
        AppConfig.print(String.format("Adding snapshot for servent %s (%d bitcakes)", servent, amount));
    }

    public void startCollecting() {
        boolean oldValue = collecting.getAndSet(true);

        if (oldValue) {
            AppConfig.error("Already snapshotting");
        }
    }

    public void stop() {
        working = false;
    }
}