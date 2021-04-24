package app;

import cli.Parser;
import servent.ServentListener;
import servent.snapshot.SnapshotCollector;

public class ServentMain {

    public static void main(String[] args) {
        String configPath = args[0];
        int localServent = Integer.parseInt(args[1]);

        AppConfig.readConfig(configPath, localServent);

        AppConfig.print("Starting servent " + AppConfig.LOCAL_SERVENT);

        SnapshotCollector collector = new SnapshotCollector();
        new Thread(collector).start();

        ServentListener listener = new ServentListener(collector);
        new Thread(listener).start();

        Parser parser = new Parser(listener, collector);
        new Thread(parser).start();
    }
}
