package message;

import app.App;
import app.Config;
import app.ServentSingle;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class MessageListener implements Runnable {

    private volatile boolean working = true;

    @Override
    public void run() {
        int port = Config.LOCAL.getAddress().getPort();

        ServerSocket server = null;

        try {
            server = new ServerSocket(port);
            server.setSoTimeout(1000);
        } catch (IOException e) {
            App.error(String.format("Cannot open listener socket on port %s (%s)", port, e.getMessage()));
            ServentSingle.stop();
        }

        while (working) {
            try {
                Socket socket = server.accept();
                Message message = App.read(socket);
                MessageHandler.handle(message);
                socket.close();
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
