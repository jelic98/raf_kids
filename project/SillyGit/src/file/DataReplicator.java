package file;

import app.Config;

public class DataReplicator implements Runnable {

    private volatile boolean working = true;

    @Override
    public void run() {
        while (working) {
            Config.STORAGE.replicate();
        }
    }

    public void stop() {
        working = false;
    }
}
