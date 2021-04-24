package snapshot;

import app.App;
import app.Config;
import app.Servent;
import message.AskMessage;
import message.BroadcastMessage;
import app.ServentState;

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

            switch (Config.SNAPSHOT_TYPE) {
                case AB:
                    BroadcastMessage message = new AskMessage(Config.LOCAL_SERVENT);

                    ServentState.commitMessage((BroadcastMessage) message.setReceiver(Config.LOCAL_SERVENT), true);

                    for (Servent neighbor : Config.LOCAL_SERVENT.getNeighbors()) {
                        App.print("Sending ASK to servent " + neighbor);
                        App.send(message.setReceiver(neighbor));
                    }

                    addSnapshot(Config.LOCAL_SERVENT, snapshotManager.getSnapshot());

                    break;
                case AV:
                    break;
            }

            while (results.size() < Config.SERVENT_COUNT) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                if (!working) {
                    return;
                }
            }

            switch (Config.SNAPSHOT_TYPE) {
                case AB:
                    int sum = 0;

                    for (Entry<Servent, Snapshot> e : results.entrySet()) {
                        int balance = e.getValue().getBalance();
                        sum += balance;
                        App.print(String.format("Servent %s has %d bitcakes", e.getKey(), balance));
                    }

                    App.print("Total bitcakes: " + sum);

                    results.clear();
                    collecting.set(false);

                    break;
                case AV:
                    break;
            }
        }
    }

    public void addSnapshot(Servent servent, Snapshot snapshot) {
        if(collecting.get()) {
            App.print(String.format("Saving snapshot for servent %s", servent));
            results.put(servent, snapshot);
        }
    }

    public void startCollecting() {
        if (collecting.getAndSet(true)) {
            App.error("Already snapshotting");
        }
    }

    public void stop() {
        working = false;
    }
}