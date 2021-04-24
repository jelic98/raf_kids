package cli.command;

public interface Command {

    String getName();

    void execute(String args);
}
