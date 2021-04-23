package servent;

import app.AppConfig;
import servent.handler.CausalBroadcastHandler;
import servent.message.Message;
import servent.message.MessageUtil;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleServentListener implements Runnable {

    private final ExecutorService threadPool = Executors.newWorkStealingPool();
    private volatile boolean working = true;

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
                }
            } catch (SocketTimeoutException timeoutEx) {
                // Ignore
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        this.working = false;
    }
}
