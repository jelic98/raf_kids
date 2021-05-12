package command;

import app.App;
import app.Config;
import app.Servent;

public class InfoCommand implements Command {

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public void execute(String args) {
        App.print("Hash: " + Config.LOCAL.getChordId() + "/" + Config.CHORD_SIZE);

        StringBuilder sb = new StringBuilder();

        sb.append("Successors:");

        for (Servent servent : Config.CHORD.getSuccessors()) {
            sb.append(" ");
            sb.append(servent);
        }

        App.print(sb.toString());
    }
}
