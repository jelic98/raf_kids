package cli;

import app.AppConfig;
import cli.command.*;
import servent.ServentListener;
import servent.snapshot.SnapshotCollector;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Parser implements Runnable {

    private final List<Command> commandList;
    private volatile boolean working = true;

    public Parser(ServentListener listener, SnapshotCollector collector) {
        commandList = new ArrayList<>();
        commandList.add(new BitcakeInfoCommand(collector));
        commandList.add(new CausalBroadcastCommand());
        commandList.add(new InfoCommand());
        commandList.add(new PauseCommand());
        commandList.add(new PrintCausalCommand());
        commandList.add(new StopCommand(this, listener, collector));
        commandList.add(new TransactionBurstCommand(collector.getSnapshotManager()));
    }

    @Override
    public void run() {
        Scanner sc = new Scanner(System.in);

        while (working) {
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
                AppConfig.error("Unknown command: " + commandName);
            }
        }

        sc.close();
    }

    public void stop() {
        working = false;
    }
}
