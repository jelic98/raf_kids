package message;

import app.App;
import app.Config;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketTimeoutException;

public class MessageListener implements Runnable {

    private volatile boolean working = true;

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
                MessageHandler.handle(message);
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
