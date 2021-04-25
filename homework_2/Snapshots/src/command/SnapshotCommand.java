package command;

import app.App;
import snapshot.SnapshotCollector;

public class SnapshotCommand implements Command {

    private SnapshotCollector collector;

    public SnapshotCommand(SnapshotCollector collector) {
        this.collector = collector;
    }

    @Override
    public String getName() {
        return "snapshot";
    }

    @Override
    public void execute(String args) {
        App.print("Snapshotting");

        collector.startCollecting();
    }
}
