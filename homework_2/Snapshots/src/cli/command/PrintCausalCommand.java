package cli.command;

import app.AppConfig;
import servent.message.CausalBroadcastMessage;
import servent.snapshot.CausalBroadcastShared;

import java.util.List;

public class PrintCausalCommand implements Command {

    @Override
    public String getName() {
        return "print_causal";
    }

    @Override
    public void execute(String args) {
        AppConfig.print("PENDING messages:");
        printMessages(CausalBroadcastShared.getPendingMessages());

        AppConfig.print("COMMITTED messages:");
        printMessages(CausalBroadcastShared.getCommittedMessages());
    }

    private void printMessages(List<CausalBroadcastMessage> messages) {
        int i = 1;

        for (CausalBroadcastMessage message : messages) {
            AppConfig.print(String.format("Message %d: %s from %s", i, message.getText(), message.getSender()));
            i++;
        }
    }
}
