package command;

import app.App;
import app.Config;
import app.Servent;
import app.ServentState;
import message.BroadcastMessage;

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

        App.print("PENDING messages:");
        printMessages(ServentState.getPendingMessages());

        App.print("COMMITTED messages:");
        printMessages(ServentState.getCommittedMessages());

        App.print("Clock (received): " + ServentState.getClockReceived());
    }

    private void printMessages(List<BroadcastMessage> messages) {
        int i = 1;

        for (BroadcastMessage message : messages) {
            App.print(String.format("Message %d: %s from %s", i, message, message.getSender()));
            i++;
        }
    }
}
