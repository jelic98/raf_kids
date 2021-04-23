package cli.command;

import app.AppConfig;

public class InfoCommand implements Command {

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public void execute(String args) {
        AppConfig.timestampedStandardPrint("My info: " + AppConfig.myServentInfo);
        AppConfig.timestampedStandardPrint("Neighbors:");

        StringBuilder sb = new StringBuilder();

        for (Integer neighbor : AppConfig.myServentInfo.getNeighbors()) {
            sb.append(neighbor);
            sb.append(" ");
        }

        AppConfig.timestampedStandardPrint(sb.toString());
    }
}
