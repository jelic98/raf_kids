package servent;

import app.AppConfig;
import servent.handler.CausalBroadcastHandler;
import servent.handler.TellHandler;
import servent.handler.TransactionHandler;
import servent.message.CausalBroadcastMessage;
import servent.message.Message;
import servent.message.MessageUtil;
import servent.snapshot.SnapshotCollector;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
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
                Socket client = server.accept();
                Message message = MessageUtil.readMessage(client);

                switch (message.getType()) {
                    case ASK:
                    case CAUSAL_BROADCAST:
                        threadPool.submit(new CausalBroadcastHandler((CausalBroadcastMessage) message, collector.getBitcakeManager()));
                        break;
                    case TRANSACTION:
                        threadPool.submit(new TransactionHandler(message, collector.getBitcakeManager()));
                        break;
                    case TELL:
                        threadPool.submit(new TellHandler(message, collector));
                        break;
                }
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
