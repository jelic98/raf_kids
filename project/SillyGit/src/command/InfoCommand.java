package command;

import app.App;
import app.Config;
import data.Hash;

public class InfoCommand implements Command {

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public void execute(String args) {
        App.print("Hash: " + Hash.get(Config.LOCAL));
        App.print("Workspace: " + Config.WORKSPACE);
        App.print("Storage: " + Config.STORAGE);
    }
}
