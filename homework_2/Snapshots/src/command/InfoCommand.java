package command;

import app.App;
import app.Config;
import app.Servent;
import app.ServentState;
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

        sb.append("Neighbors:");

        for (Servent neighbor : Config.LOCAL_SERVENT.getNeighbors()) {
            sb.append(" ");
            sb.append(neighbor);
        }

        App.print(sb.toString());

        printMessages(ServentState.getPendingMessages(), "PENDING");
        printMessages(ServentState.getCommittedMessages(), "COMMITTED");

        App.print("Clock: " + ServentState.getClock());
    }

    private void printMessages(List<Message> messages, String type) {
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
