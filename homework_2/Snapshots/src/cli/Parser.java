package cli;

import app.AppConfig;
import cli.command.*;
import servent.SimpleServentListener;
import servent.snapshot.SnapshotCollector;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * A simple CLI parser. Each command has a name and arbitrary arguments.
 * <p>
 * Currently supported commands:
 *
 * <ul>
 * <li><code>info</code> - prints information about the current node</li>
 * <li><code>pause [ms]</code> - pauses exection given number of ms - useful when scripting</li>
 * <li><code>ping [id]</code> - sends a PING message to node [id] </li>
 * <li><code>broadcast [text]</code> - broadcasts the given text to all nodes</li>
 * <li><code>causal_broadcast [text]</code> - causally broadcasts the given text to all nodes</li>
 * <li><code>print_causal</code> - prints all received causal broadcast messages</li>
 * <li><code>stop</code> - stops the servent and program finishes</li>
 * </ul>
 *
 * @author bmilojkovic
 */
public class Parser implements Runnable {

    private final List<Command> commandList;
    private volatile boolean working = true;

    public Parser(SimpleServentListener listener, SnapshotCollector collector) {
        commandList = new ArrayList<>();
        commandList.add(new BitcakeInfoCommand(collector));
        commandList.add(new CausalBroadcastCommand());
        commandList.add(new InfoCommand());
        commandList.add(new PauseCommand());
        commandList.add(new PrintCausalCommand());
        commandList.add(new StopCommand(this, listener, collector));
        commandList.add(new TransactionBurstCommand(collector.getBitcakeManager()));
    }

    @Override
    public void run() {
        Scanner sc = new Scanner(System.in);

        while (working) {
            String commandLine = sc.nextLine();

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
                AppConfig.timestampedErrorPrint("Unknown command: " + commandName);
            }
        }

        sc.close();
    }

    public void stop() {
        working = false;
    }
}
