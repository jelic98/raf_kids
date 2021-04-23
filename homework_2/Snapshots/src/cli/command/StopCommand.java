package cli.command;

import app.AppConfig;
import cli.Parser;
import servent.ServentListener;
import servent.snapshot.SnapshotCollector;

public class StopCommand implements Command {

    private final Parser parser;
    private final ServentListener listener;
    private final SnapshotCollector collector;

    public StopCommand(Parser parser, ServentListener listener, SnapshotCollector collector) {
        this.parser = parser;
        this.listener = listener;
        this.collector = collector;
    }

    @Override
    public String getName() {
        return "stop";
    }

    @Override
    public void execute(String args) {
        AppConfig.timestampedStandardPrint("Stopping...");
        parser.stop();
        listener.stop();
        collector.stop();
    }
}
