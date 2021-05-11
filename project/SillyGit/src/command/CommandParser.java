package command;

import app.App;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CommandParser implements Runnable {

    private final List<Command> commandList;
    private volatile boolean working = true;

    public CommandParser() {
        commandList = new ArrayList<>();
        commandList.add(new BroadcastCommand());
        commandList.add(new InfoCommand());
        commandList.add(new PauseCommand());
        commandList.add(new StopCommand());
    }

    @Override
    public void run() {
        Scanner sc = new Scanner(System.in);

        while (working && sc.hasNextLine()) {
            String line = sc.nextLine();

            if (line.startsWith("#")) {
                continue;
            }

            String[] tokens = line.split(" ");
            String name = tokens[0];
            String args = null;

            if(tokens.length > 1) {
                args = line.substring(name.length() + 1);
            }

            boolean found = false;

            for (Command command : commandList) {
                if (command.getName().equals(name)) {
                    command.execute(args);
                    found = true;
                    break;
                }
            }

            if (!found) {
                App.error("Unknown command: " + name);
            }
        }

        sc.close();
    }

    public void stop() {
        working = false;
    }
}
