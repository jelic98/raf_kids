package command;

import app.App;
import app.Config;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CommandParser implements Runnable {

    private static final Queue<Prompt> prompts = new ConcurrentLinkedQueue<>();

    private final List<Command> commandList;
    private volatile boolean working = true;

    public CommandParser() {
        commandList = new ArrayList<>();

        if(!Config.LOCAL.equals(Config.BOOTSTRAP)) {
            commandList.add(new AddCommand());
            commandList.add(new InfoCommand());
            commandList.add(new PauseCommand());
            commandList.add(new PullCommand());
            commandList.add(new PushCommand());
            commandList.add(new RemoveCommand());
        }

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

            if (tokens.length > 1) {
                args = line.substring(name.length() + 1);
            }

            if (!prompts.isEmpty() && prompts.poll().select(name)) {
                continue;
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

    public static void addPrompt(Prompt prompt) {
        App.print(prompt.toString());
        prompts.add(prompt);
    }
}
