package servent.snapshot;

import app.AppConfig;
import servent.message.AskMessage;
import servent.message.Message;
import servent.message.MessageUtil;

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
    private BitcakeManager bitcakeManager;

    public SnapshotCollector() {
        switch(AppConfig.SNAPSHOT_TYPE) {
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
            if(!collecting.get()) {
                continue;
            }

            AppConfig.timestampedStandardPrint("Sending ASK messages");

            switch (AppConfig.SNAPSHOT_TYPE) {
                case AB:
                    Message message = new AskMessage(AppConfig.myServentInfo, null);

                    for (Integer neighbor : AppConfig.myServentInfo.getNeighbors()) {
                        MessageUtil.sendMessage(message.changeReceiver(neighbor));
                    }

                    addSnapshot(AppConfig.myServentInfo.getId(), bitcakeManager.getCurrentBitcakeAmount());

                    break;
                case AV:
                    break;
            }

            AppConfig.timestampedStandardPrint("Waiting for TELL messages");

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

            AppConfig.timestampedStandardPrint("Gathering results");

            switch (AppConfig.SNAPSHOT_TYPE) {
                case AB:
                    int sum = 0;

                    for (Entry<Integer, Integer> e : results.entrySet()) {
                        sum += e.getValue();
                        AppConfig.timestampedStandardPrint(String.format("Servent %d has %d bitcakes", e.getKey(), e.getValue()));
                    }

                    AppConfig.timestampedStandardPrint("Total bitcakes: " + sum);

                    results.clear();
                    collecting.set(false);

                    break;
                case AV:
                    break;
            }
        }
    }

    public void addSnapshot(int id, int amount) {
        results.put(id, amount);
        AppConfig.timestampedStandardPrint(String.format("Adding snapshot for servent %d (%d bitcakes)", id, amount));
    }

    public void startCollecting() {
        boolean oldValue = collecting.getAndSet(true);

        if (oldValue) {
            AppConfig.timestampedErrorPrint("Tried to start collecting before finished with previous.");
        }
    }

    public void stop() {
        working = false;
    }
}