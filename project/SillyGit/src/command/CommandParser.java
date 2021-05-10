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
            String commandLine = sc.nextLine();

            if (commandLine.startsWith("#")) {
                continue;
            }

            int spacePos = commandLine.indexOf(" ");

            String commandName;
            String commandArgs = null;
            if (spacePos != -1) {
                commandName = commandLine.substring(0, spacePos);
                commandArgs = commandLine.substring(spacePos + 1);
            } else {
                commandName = commandLine;
            }

            boolean found = false;

            for (Command command : commandList) {
                if (command.getName().equals(commandName)) {
                    command.execute(commandArgs);
                    found = true;
                    break;
                }
            }

            if (!found) {
                App.error("Unknown command: " + commandName);
            }
        }

        sc.close();
    }

    public void stop() {
        working = false;
    }
}
