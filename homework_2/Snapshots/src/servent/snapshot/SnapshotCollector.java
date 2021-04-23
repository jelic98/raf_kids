package servent.snapshot;

import app.AppConfig;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Main snapshot collector class.
 *
 * @author bmilojkovic
 *
 */
public class SnapshotCollector implements Runnable {

    private volatile boolean working = true;
    private AtomicBoolean collecting = new AtomicBoolean(false);
    private Map<Integer, Integer> results = new ConcurrentHashMap<>();
    private SnapshotType snapshotType;
    private BitcakeManager bitcakeManager;

    public SnapshotCollector(SnapshotType snapshotType) {
        this.snapshotType = snapshotType;

        switch(snapshotType) {
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
        while(working) {

            while (!collecting.get()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                if (!working) {
                    return;
                }
            }

            switch (snapshotType) {
//                Message askMessage = new AskMessage(AppConfig.myServentInfo, null);
//                for (Integer neighbor : AppConfig.myServentInfo.getNeighbors()) {
//                    MessageUtil.sendMessage(askMessage.changeReceiver(neighbor));
//                }
//                addSnapshot(AppConfig.myServentInfo.getId(), bitcakeManager.getCurrentBitcakeAmount());
                case AB:
                    break;
                case AV:
                    break;
            }

            boolean waiting = true;

            while (waiting) {
                if (results.size() == AppConfig.getServentCount()) {
                    waiting = false;
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                if (!working) {
                    return;
                }
            }

            int sum = 0;

            for (Entry<Integer, Integer> itemAmount : results.entrySet()) {
                sum += itemAmount.getValue();
                AppConfig.timestampedStandardPrint(
                        "Info for " + itemAmount.getKey() + " = " + itemAmount.getValue() + " bitcake");
            }

            AppConfig.timestampedStandardPrint("System bitcake count: " + sum);

            results.clear();
            collecting.set(false);
        }

    }

    public void addSnapshot(int id, int amount) {
        results.put(id, amount);
    }

    public void startCollecting() {
        boolean oldValue = this.collecting.getAndSet(true);

        if (oldValue) {
            AppConfig.timestampedErrorPrint("Tried to start collecting before finished with previous.");
        }
    }

    public void stop() {
        working = false;
    }
}