package app;

import command.CommandParser;
import message.MessageListener;

public class ServentSingle {

    private static MessageListener listener;
    private static CommandParser parser;

    public static void main(String[] args) {
        Config.load(args[0], Integer.parseInt(args[1]));

        App.print("Starting servent " + Config.LOCAL_SERVENT);

        listener = new MessageListener();
        new Thread(listener).start();

        parser = new CommandParser();
        new Thread(parser).start();

    }

    public static void stop() {
        App.print("Stopping");

        listener.stop();
        parser.stop();
    }
}
