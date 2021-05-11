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
        StringBuilder sb = new StringBuilder();

        sb.append("Successors:");

        for (Servent servent : Config.CHORD.getSuccessors()) {
            sb.append(" ");
            sb.append(servent);
        }

        App.print(sb.toString());

        printMessages(ServentState.getPendingMessages(), "PENDING");
        printMessages(ServentState.getCommittedMessages(), "COMMITTED");

        App.print("Clock: " + ServentState.getClock());
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
