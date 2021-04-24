package command;

import message.MessageListener;
import app.App;
import snapshot.SnapshotCollector;

public class StopCommand implements Command {

    private final CommandParser parser;
    private final MessageListener listener;
    private final SnapshotCollector collector;

    public StopCommand(CommandParser parser, MessageListener listener, SnapshotCollector collector) {
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
        parser.stop();
        listener.stop();
        collector.stop();
        App.print("Stopped");
    }
}
