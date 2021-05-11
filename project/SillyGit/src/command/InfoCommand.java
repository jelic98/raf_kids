package command;

import app.App;
import app.Config;
import app.Servent;
import app.ServentState;
import message.BroadcastMessage;
import message.Message;

import java.util.List;

public class InfoCommand implements Command {

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public void execute(String args) {
        printServents(Config.CHORD.getSuccessors(), "Successors");

        printMessages(ServentState.getPendingMessages(), "PENDING");
        printMessages(ServentState.getCommittedMessages(), "COMMITTED");

        App.print("Clock: " + ServentState.getClock());
    }

    private void printServents(Servent[] servents, String type) {
        StringBuilder sb = new StringBuilder();

        sb.append(type);
        sb.append(":");

        for (Servent s : servents) {
            sb.append(" ");
            sb.append(s);
        }

        App.print(sb.toString());
    }

    private void printMessages(List<BroadcastMessage> messages, String type) {
        if (messages.isEmpty()) {
            App.print("No " + type + " messages");
            return;
        }

        App.print(type + " messages:");

        int i = 1;

        for (Message message : messages) {
            App.print(String.format("Message %d: %s from %s", i, message, message.getSender()));
            i++;
        }
    }
}
