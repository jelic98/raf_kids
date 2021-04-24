package cli.command;

import app.AppConfig;
import app.Servent;

public class InfoCommand implements Command {

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public void execute(String args) {
        AppConfig.print("Info: " + AppConfig.LOCAL_SERVENT);

        StringBuilder sb = new StringBuilder();

        sb.append("Neighbors:");

        for (Servent neighbor : AppConfig.LOCAL_SERVENT.getNeighbors()) {
            sb.append(" ");
            sb.append(neighbor);
        }

        AppConfig.print(sb.toString());
    }
}
