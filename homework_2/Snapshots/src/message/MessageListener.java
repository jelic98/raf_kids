package message;

import app.App;
import app.Config;
import snapshot.SnapshotCollector;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MessageListener implements Runnable {

    private final ExecutorService threadPool = Executors.newWorkStealingPool();
    private SnapshotCollector collector;
    private volatile boolean working = true;

    public MessageListener(SnapshotCollector collector) {
        this.collector = collector;
    }

    @Override
    public void run() {
        ServerSocket server = null;

        try {
            server = new ServerSocket(Config.LOCAL_SERVENT.getPort());
            server.setSoTimeout(1000);
        } catch (IOException e) {
            App.error("Cannot open listener socket on port " + Config.LOCAL_SERVENT.getPort());
            System.exit(0);
        }

        while (working) {
            try {
                Message message = App.read(server.accept());
                threadPool.submit(new MessageHandler((BroadcastMessage) message, collector));
            } catch (SocketTimeoutException timeoutEx) {
                // Ignore
            } catch (IOException e) {
                App.error("Error while listening socket");
            }
        }
    }

    public void stop() {
        working = false;
    }
}
