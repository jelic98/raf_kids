package app;

import command.CommandParser;
import file.Files;
import message.JoinAskMessage;
import message.MessageHandler;
import message.MessageListener;

import java.io.File;

public class ServentSingle {

    private static MessageHandler handler;
    private static MessageListener listener;
    private static CommandParser parser;
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

        if (isServent) {
            parser = new CommandParser();
            new Thread(parser).start();

            App.send(new JoinAskMessage());

            new File(Files.absolute(Config.WORKSPACE_PATH, "")).mkdirs();
            new File(Files.absolute(Config.STORAGE_PATH, "")).mkdirs();

            App.print("Started servent " + Config.LOCAL);
        } else {
            App.print("Started bootstrap server " + Config.LOCAL);
        }
    }

    public static void stop() {
        if (isServent) {
            parser.stop();
        }

        listener.stop();
        handler.stop();

        if (isServent) {
            App.print("Servent stopped");
        } else {
            App.print("Bootstrap stopped");
        }
    }
}
