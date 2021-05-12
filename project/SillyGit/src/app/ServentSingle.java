package app;

import command.CommandParser;
import message.HailAskMessage;
import message.MessageHandler;
import message.MessageListener;

public class ServentSingle {

    private static MessageListener listener;
    private static CommandParser parser;
    private static boolean isServent;

    public static void main(String[] args) {
        int servent = Integer.parseInt(args[1]);

        Config.load(args[0], servent);

        isServent = servent > 0;

        if (isServent) {
            App.print("Starting servent " + Config.LOCAL_SERVENT);
        } else {
            Config.LOCAL_SERVENT = Config.BOOTSTRAP_SERVER;

            App.print("Starting bootstrap server " + Config.LOCAL_SERVENT);
        }

        new Thread(new MessageHandler()).start();

        listener = new MessageListener();
        new Thread(listener).start();

        if (isServent) {
            parser = new CommandParser();
            new Thread(parser).start();

            App.send(new HailAskMessage());
        }
    }

    public static void stop() {
        App.print("Stopping");

        listener.stop();

        if (isServent) {
            parser.stop();
        }

        System.exit(0);
    }
}
