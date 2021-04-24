package servent.handler;

import app.AppConfig;
import app.Servent;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.MessageUtil;
import servent.snapshot.CausalBroadcastShared;
import servent.snapshot.SnapshotCollector;

public class TellHandler implements Runnable {

    private Message clientMessage;
    private SnapshotCollector snapshotCollector;

    public TellHandler(Message clientMessage, SnapshotCollector snapshotCollector) {
        this.clientMessage = clientMessage;
        this.snapshotCollector = snapshotCollector;
    }

    @Override
    public void run() {
        if (clientMessage.getType() == MessageType.TELL) {
            Servent sender = CausalBroadcastShared.getAskSender();

            if (sender.equals(AppConfig.LOCAL_SERVENT)) {
                snapshotCollector.addSnapshot(clientMessage.getSender(), Integer.parseInt(clientMessage.getText()));
            } else {
                AppConfig.print(String.format("Redirecting TELL from %s to %s", clientMessage.getSender(), sender));
                MessageUtil.sendMessage(clientMessage.setReceiver(sender).setSender());
            }
        }
    }
}
