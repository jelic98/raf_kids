package cli.command;

import app.AppConfig;
import app.CausalBroadcastShared;
import servent.message.Message;

public class PrintCausalCommand implements CLICommand {

    @Override
    public String commandName() {
        return "print_causal";
    }

    @Override
    public void execute(String args) {
        int i = 1;
        AppConfig.timestampedStandardPrint("Current PENDING messages:");
        for (Message message : CausalBroadcastShared.getPendingCausalMessages()) {
            AppConfig.timestampedStandardPrint("Message " + i + ": " + message.getMessageText() +
                    " from " + message.getOriginalSenderInfo().getId());
            i++;
        }
        i = 1;
        AppConfig.timestampedStandardPrint("Current COMMITTED messages:");
        for (Message message : CausalBroadcastShared.getCommittedCausalMessages()) {
            AppConfig.timestampedStandardPrint("Message " + i + ": " + message.getMessageText() +
                    " from " + message.getOriginalSenderInfo().getId());
            i++;
        }
    }

}
