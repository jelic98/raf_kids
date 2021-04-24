package cli.command;

import app.AppConfig;
import servent.message.BroadcastMessage;
import servent.snapshot.BroadcastShared;

import java.util.List;

public class PrintMessagesCommand implements Command {

    @Override
    public String getName() {
        return "print_messages";
    }

    @Override
    public void execute(String args) {
        AppConfig.print("PENDING messages:");
        printMessages(BroadcastShared.getPendingMessages());

        AppConfig.print("COMMITTED messages:");
        printMessages(BroadcastShared.getCommittedMessages());
    }

    private void printMessages(List<BroadcastMessage> messages) {
        int i = 1;

        for (BroadcastMessage message : messages) {
            AppConfig.print(String.format("Message %d: %s from %s", i, message.getText(), message.getSender()));
            i++;
        }
    }
}
