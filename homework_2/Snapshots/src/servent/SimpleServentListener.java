package servent;

import app.AppConfig;
import servent.handler.AskHandler;
import servent.handler.CausalBroadcastHandler;
import servent.handler.TellHandler;
import servent.handler.TransactionHandler;
import servent.message.Message;
import servent.message.MessageUtil;
import servent.snapshot.SnapshotCollector;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleServentListener implements Runnable {

    private final ExecutorService threadPool = Executors.newWorkStealingPool();
    private SnapshotCollector snapshotCollector;
    private volatile boolean working = true;

    public SimpleServentListener(SnapshotCollector snapshotCollector) {
        this.snapshotCollector = snapshotCollector;
    }

    @Override
    public void run() {
        ServerSocket listenerSocket = null;
        try {
            listenerSocket = new ServerSocket(AppConfig.myServentInfo.getListenerPort());
            listenerSocket.setSoTimeout(1000);
        } catch (IOException e) {
            AppConfig.timestampedErrorPrint("Couldn't open listener socket on: " + AppConfig.myServentInfo.getListenerPort());
            System.exit(0);
        }

        while (working) {
            try {
                Socket clientSocket = listenerSocket.accept();

                Message clientMessage = MessageUtil.readMessage(clientSocket);

                switch (clientMessage.getMessageType()) {
                    case CAUSAL_BROADCAST:
                        threadPool.submit(new CausalBroadcastHandler(clientMessage));
                        break;
                    case TRANSACTION:
                        threadPool.submit(new TransactionHandler(clientMessage, snapshotCollector.getBitcakeManager()));
                        break;
                    case ASK:
                        threadPool.submit(new AskHandler(clientMessage, snapshotCollector.getBitcakeManager()));
                        break;
                    case TELL:
                        threadPool.submit(new TellHandler(clientMessage, snapshotCollector));
                        break;
                }
            } catch (SocketTimeoutException timeoutEx) {
                // Ignore
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        working = false;
    }
}
