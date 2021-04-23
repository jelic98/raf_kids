package app.snapshot_bitcake;

import app.AppConfig;
import servent.message.Message;
import servent.message.snapshot.LYMarkerMessage;
import servent.message.snapshot.LYTellMessage;
import servent.message.util.MessageUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;

public class LYBitcakeManager implements BitcakeManager {

    private final AtomicInteger currentAmount = new AtomicInteger(1000);
    /*
     * This value is protected by AppConfig.colorLock.
     * Access it only if you have the blessing.
     */
    public int recordedAmount = 0;
    private final Map<Integer, Integer> giveHistory = new ConcurrentHashMap<>();
    private final Map<Integer, Integer> getHistory = new ConcurrentHashMap<>();

    public LYBitcakeManager() {
        for (Integer neighbor : AppConfig.myServentInfo.getNeighbors()) {
            giveHistory.put(neighbor, 0);
            getHistory.put(neighbor, 0);
        }
    }

    public void takeSomeBitcakes(int amount) {
        currentAmount.getAndAdd(-amount);
    }

    public void addSomeBitcakes(int amount) {
        currentAmount.getAndAdd(amount);
    }

    public void markerEvent(int collectorId, SnapshotCollector snapshotCollector) {
        synchronized (AppConfig.colorLock) {
            AppConfig.isWhite.set(false);
            recordedAmount = currentAmount.get();

            LYSnapshotResult snapshotResult = new LYSnapshotResult(recordedAmount, giveHistory, getHistory);

            if (collectorId == AppConfig.myServentInfo.getId()) {
                snapshotCollector.addLYSnapshotInfo(
                        AppConfig.myServentInfo.getId(),
                        snapshotResult);
            } else {

                Message tellMessage = new LYTellMessage(
                        AppConfig.myServentInfo, AppConfig.getInfoById(collectorId), snapshotResult);

                MessageUtil.sendMessage(tellMessage);
            }

            for (Integer neighbor : AppConfig.myServentInfo.getNeighbors()) {
                Message clMarker = new LYMarkerMessage(AppConfig.myServentInfo, AppConfig.getInfoById(neighbor), collectorId);
                MessageUtil.sendMessage(clMarker);
                try {
                    //Artificially produce some white node -> red node messages
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public void recordGiveTransaction(int neighbor, int amount) {
        giveHistory.compute(neighbor, new MapValueUpdater(amount));
    }

    public void recordGetTransaction(int neighbor, int amount) {
        getHistory.compute(neighbor, new MapValueUpdater(amount));
    }

    private static class MapValueUpdater implements BiFunction<Integer, Integer, Integer> {

        private final int valueToAdd;

        public MapValueUpdater(int valueToAdd) {
            this.valueToAdd = valueToAdd;
        }

        @Override
        public Integer apply(Integer key, Integer oldValue) {
            return oldValue + valueToAdd;
        }
    }
}
