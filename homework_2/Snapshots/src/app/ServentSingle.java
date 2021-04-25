package app;

import command.CommandParser;
import message.MessageListener;
import snapshot.SnapshotCollector;

public class ServentSingle {

    private static SnapshotCollector collector;
    private static MessageListener listener;
    private static CommandParser parser;

    public static void main(String[] args) {
        Config.load(args[0], Integer.parseInt(args[1]));

        App.print("Starting servent " + Config.LOCAL_SERVENT);

        collector = new SnapshotCollector();
        new Thread(collector).start();

        listener = new MessageListener(collector);
        new Thread(listener).start();

        parser = new CommandParser(collector);
        new Thread(parser).start();
    }

    public static void stop() {
        App.print("Stopping");

        collector.stop();
        listener.stop();
        parser.stop();
    }
}
