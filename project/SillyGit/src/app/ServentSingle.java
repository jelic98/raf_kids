package app;

import command.CommandParser;
import file.DataReplicator;
import file.Files;
import message.JoinAskMessage;
import message.MessageHandler;
import message.MessageListener;
import servent.ServentPinger;

import java.io.File;

public class ServentSingle {

    private static MessageHandler handler;
    private static MessageListener listener;
    private static CommandParser parser;
    private static ServentPinger pinger;
    private static DataReplicator replicator;
    private static boolean isServent;

    public static void main(String[] args) {
        int servent = Integer.parseInt(args[1]);

        Config.load(args[0], servent);

        isServent = servent > 0;

        if (!isServent) {
            Config.LOCAL = Config.BOOTSTRAP;
        }

        handler = new MessageHandler();
        new Thread(handler).start();

        listener = new MessageListener();
        new Thread(listener).start();

        parser = new CommandParser();
        new Thread(parser).start();

        if (isServent) {
            pinger = new ServentPinger();
            new Thread(pinger).start();

            replicator = new DataReplicator();
            new Thread(replicator).start();

            new File(Files.absolute(Config.WORKSPACE_PATH, "")).mkdirs();
            new File(Files.absolute(Config.STORAGE_PATH, "")).mkdirs();

            App.print("Started servent " + Config.LOCAL);

            App.send(new JoinAskMessage());
        } else {
            App.print("Started bootstrap server " + Config.LOCAL);
        }
    }

    public static void stop() {
        if (isServent) {
            replicator.stop();
            pinger.stop();
        }

        parser.stop();
        listener.stop();
        handler.stop();

        if (isServent) {
            App.print("Servent stopped");
        } else {
            App.print("Bootstrap stopped");
        }

        System.exit(0);
    }
}
