package app;

import cli.Parser;
import servent.ServentListener;
import servent.snapshot.SnapshotCollector;

/**
 * Describes the procedure for starting a single Servent
 *
 * @author bmilojkovic
 */
public class ServentMain {

    /**
     * Command line arguments are:
     * 0 - path to servent list file
     * 1 - this servent's id
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            AppConfig.timestampedErrorPrint("Please provide servent list file and id of this servent.");
        }

        String serventListFile = args[0];
        int serventId = Integer.parseInt(args[1]);

        AppConfig.readConfig(serventListFile);
        AppConfig.myServentInfo = AppConfig.getInfoById(serventId);

        AppConfig.timestampedStandardPrint("Starting servent " + AppConfig.myServentInfo);

        SnapshotCollector collector = new SnapshotCollector();
        new Thread(collector).start();

        ServentListener listener = new ServentListener(collector);
        new Thread(listener).start();

        Parser parser = new Parser(listener, collector);
        new Thread(parser).start();
    }
}
