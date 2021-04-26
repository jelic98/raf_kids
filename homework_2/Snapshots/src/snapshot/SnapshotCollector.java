package snapshot;

import app.App;
import app.Config;
import app.Servent;
import message.AskMessage;
import message.MessageHandler;
import message.TerminateMessage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class SnapshotCollector implements Runnable {

    private final Map<Servent, Snapshot> state;
    private final AtomicBoolean collecting;
    private volatile boolean working = true;

    public SnapshotCollector() {
        state = new ConcurrentHashMap<>();
        collecting = new AtomicBoolean(false);
    }

    @Override
    public void run() {
        while (working) {
            if (!collecting.get()) {
                continue;
            }

            sendAskMessage();
            receiveTellMessages();

            if (Config.SNAPSHOT_TYPE == SnapshotType.AV) {
                sendTerminateMessage();
            }

            calculateState();
            stopSnapshot();
        }
    }

    public void addSnapshot(Servent servent, Snapshot snapshot) {
        if (collecting.get()) {
            state.put(servent, snapshot);
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

    private void sendAskMessage() {
        MessageHandler.handle(new AskMessage());
    }

    private void sendTerminateMessage() {
        MessageHandler.handle(new TerminateMessage(new ConcurrentHashMap<>(state)));
    }

    private void receiveTellMessages() {
        while (state.size() < Config.SERVENT_COUNT) {
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

    private void calculateState() {
        int sum = 0;

        for (Map.Entry<Servent, Snapshot> e : state.entrySet()) {
            int balance = e.getValue().getBalance();
            sum += balance;
            App.print(String.format("Servent %s has %d bitcakes", e.getKey(), balance));
        }

        if (Config.SNAPSHOT_TYPE == SnapshotType.AB) {
            for (int i = 0; i < Config.SERVENT_COUNT; i++) {
                for (int j = 0; j < Config.SERVENT_COUNT; j++) {
                    if (i == j) {
                        continue;
                    }

                    Servent from = Config.SERVENTS.get(i);
                    Servent to = Config.SERVENTS.get(j);

                    int diff = state.get(from).getMinusHistory().get(to) - state.get(to).getPlusHistory().get(from);

                    if (diff > 0) {
                        App.print(String.format("Servent %s has %d unreceived bitcakes from %s", to, diff, from));
                        sum += diff;
                    }
                }
            }
        }

        App.print("Total bitcakes: " + sum);
    }

    private void stopSnapshot() {
        state.clear();
        collecting.set(false);
    }
}