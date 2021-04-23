package cli.command;

import app.AppConfig;
import cli.Parser;
import servent.SimpleServentListener;

public class StopCommand implements Command {

    private final Parser parser;
    private final SimpleServentListener listener;

    public StopCommand(Parser parser, SimpleServentListener listener) {
        this.parser = parser;
        this.listener = listener;
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
    }
}
