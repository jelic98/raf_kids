package command;

import app.App;
import app.Config;

public class InfoCommand implements Command {

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public void execute(String args) {
        App.print("Hash: " + Config.LOCAL.hashCode());
    }
}
