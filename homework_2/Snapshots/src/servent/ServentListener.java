package servent;

import app.AppConfig;
import servent.handler.MessageHandler;
import servent.message.*;
import servent.snapshot.SnapshotCollector;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServentListener implements Runnable {

    private final ExecutorService threadPool = Executors.newWorkStealingPool();
    private SnapshotCollector collector;
    private volatile boolean working = true;

    public ServentListener(SnapshotCollector collector) {
        this.collector = collector;
    }

    @Override
    public void run() {
        ServerSocket server = null;

        try {
            server = new ServerSocket(AppConfig.LOCAL_SERVENT.getPort());
            server.setSoTimeout(1000);
        } catch (IOException e) {
            AppConfig.error("Cannot open listener socket on port " + AppConfig.LOCAL_SERVENT.getPort());
            System.exit(0);
        }

        while (working) {
            try {
                Message message = MessageUtil.readMessage(server.accept());
                threadPool.submit(new MessageHandler((BroadcastMessage) message, collector));
            } catch (SocketTimeoutException timeoutEx) {
                // Ignore
            } catch (IOException e) {
                AppConfig.error("Error while listening socket");
            }
        }
    }

    public void stop() {
        working = false;
    }
}
