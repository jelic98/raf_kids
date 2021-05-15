package servent;

import app.Config;

public class ServentPinger implements Runnable {

    private volatile boolean working = true;

    @Override
    public void run() {
        while (working) {
            Config.SYSTEM.ping();
        }
    }

    public void stop() {
        working = false;
    }
}
