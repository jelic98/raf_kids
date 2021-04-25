package app;

import command.CommandParser;
import message.MessageListener;
import snapshot.SnapshotCollector;

public class ServentSingle {

    public static void main(String[] args) {
        String configPath = args[0];
        int localServent = Integer.parseInt(args[1]);

        Config.load(configPath, localServent);

        App.print("Starting servent " + Config.LOCAL_SERVENT);

        SnapshotCollector collector = new SnapshotCollector();
        new Thread(collector).start();

        MessageListener listener = new MessageListener(collector);
        new Thread(listener).start();

        CommandParser parser = new CommandParser(listener, collector);
        new Thread(parser).start();
    }
}
