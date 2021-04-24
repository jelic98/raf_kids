package cli.command;

import app.AppConfig;
import servent.snapshot.SnapshotCollector;

public class BitcakeInfoCommand implements Command {

    private SnapshotCollector collector;

    public BitcakeInfoCommand(SnapshotCollector collector) {
        this.collector = collector;
    }

    @Override
    public String getName() {
        return "bitcake_info";
    }

    @Override
    public void execute(String args) {
        AppConfig.print("Creating snapshot");

        collector.startCollecting();
    }
}
