package command;

import app.ServentState;
import app.App;
import app.Config;
import app.Servent;
import message.BroadcastMessage;

import java.util.List;

public class InfoCommand implements Command {

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public void execute(String args) {
        App.print("Servent: " + Config.LOCAL_SERVENT);

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

        App.print("util.Clock (received): " + ServentState.getClockReceived());
        App.print("util.Clock (sent): " + ServentState.getClockSent());
    }

    private void printMessages(List<BroadcastMessage> messages) {
        int i = 1;

        for (BroadcastMessage message : messages) {
            App.print(String.format("Message %d: %s from %s", i, message, message.getSender()));
            i++;
        }
    }
}
