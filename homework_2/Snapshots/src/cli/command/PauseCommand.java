package cli.command;

import app.AppConfig;

public class PauseCommand implements Command {

    @Override
    public String getName() {
        return "pause";
    }

    @Override
    public void execute(String args) {
        int millis = Integer.parseInt(args);

        AppConfig.timestampedStandardPrint("Pausing for " + millis + " ms");

        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
