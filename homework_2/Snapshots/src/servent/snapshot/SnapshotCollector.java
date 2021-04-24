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
    private Map<Servent, Snapshot> results = new ConcurrentHashMap<>();
    private SnapshotManager snapshotManager;

    public SnapshotCollector() {
        snapshotManager = new SnapshotManager();
    }

    public SnapshotManager getSnapshotManager() {
        return snapshotManager;
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

                    CausalBroadcastMessage message = new AskMessage(AppConfig.LOCAL_SERVENT);

                    for (Servent neighbor : AppConfig.LOCAL_SERVENT.getNeighbors()) {
                        AppConfig.print("Sending ASK to servent " + neighbor);
                        MessageUtil.sendMessage(message.setReceiver(neighbor));
                    }

                    CausalBroadcastShared.commitMessage((CausalBroadcastMessage) message.setReceiver(AppConfig.LOCAL_SERVENT), true);

                    addSnapshot(AppConfig.LOCAL_SERVENT, snapshotManager.getSnapshot());

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

                    for (Entry<Servent, Snapshot> e : results.entrySet()) {
                        int balance = e.getValue().getBalance();
                        sum += balance;
                        AppConfig.print(String.format("Servent %s has %d bitcakes", e.getKey(), balance));
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

    public void addSnapshot(Servent servent, Snapshot snapshot) {
        results.put(servent, snapshot);
        AppConfig.print(String.format("Adding snapshot for servent %s", servent));
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