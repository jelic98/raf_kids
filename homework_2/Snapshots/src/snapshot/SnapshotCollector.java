package snapshot;

import app.App;
import app.Config;
import app.Servent;
import app.ServentState;
import message.AskMessage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class SnapshotCollector implements Runnable {

    private final Map<Servent, Snapshot> results;
    private final AtomicBoolean collecting;
    private volatile boolean working = true;

    public SnapshotCollector() {
        results = new ConcurrentHashMap<>();
        collecting = new AtomicBoolean(false);
    }

    @Override
    public void run() {
        while (working) {
            if (!collecting.get()) {
                continue;
            }

            sendAskMessages();
            receiveTellMessages();
            calculateResult();
        }
    }

    public void addSnapshot(Servent servent, Snapshot snapshot) {
        if (collecting.get()) {
            App.print(String.format("Servent %s received %s and gave %s", servent, snapshot.getPlusHistory(), snapshot.getMinusHistory()));
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

    private void sendAskMessages() {
        ServentState.broadcast(new AskMessage());

        addSnapshot(Config.LOCAL_SERVENT, ServentState.getSnapshotManager().getSnapshot());
    }

    private void receiveTellMessages() {
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
    }

    private void calculateResult() {
        int sum = 0;

        for (Map.Entry<Servent, Snapshot> e : results.entrySet()) {
            int balance = e.getValue().getBalance();
            sum += balance;
            App.print(String.format("Servent %s has %d bitcakes", e.getKey(), balance));
        }

        for (int i = 0; i < Config.SERVENT_COUNT; i++) {
            for (int j = 0; j < Config.SERVENT_COUNT; j++) {
                if (i == j) {
                    continue;
                }

                Servent si = Config.SERVENTS.get(i);
                Servent sj = Config.SERVENTS.get(j);

                int diff = results.get(si).getMinusHistory().get(sj) - results.get(sj).getPlusHistory().get(si);

                if (diff > 0) {
                    App.print(String.format("Servent %s has %d unreceived bitcakes from %s", sj, diff, si));
                    sum += diff;
                }
            }
        }

        App.print("Total bitcakes: " + sum);

        results.clear();
        collecting.set(false);
    }
}