package command;

import app.ServentState;
import message.BroadcastMessage;
import snapshot.SnapshotCollector;

public class BroadcastCommand implements Command {

    private SnapshotCollector collector;

    public BroadcastCommand(SnapshotCollector collector) {
        this.collector = collector;
    }

    @Override
    public String getName() {
        return "broadcast";
    }

    @Override
    public void execute(String args) {
        ServentState.broadcast(new BroadcastMessage(args), collector);
    }
}
