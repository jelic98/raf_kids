package command;

import app.ServentSingle;
import app.ServentState;
import message.StopMessage;
import snapshot.SnapshotCollector;

public class StopCommand implements Command {

    private SnapshotCollector collector;

    public StopCommand(SnapshotCollector collector) {
        this.collector = collector;
    }

    @Override
    public String getName() {
        return "stop";
    }

    @Override
    public void execute(String args) {
        ServentSingle.stop();

        ServentState.broadcast(new StopMessage(), collector);
    }
}
