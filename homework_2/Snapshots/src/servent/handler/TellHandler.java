package servent.handler;

import app.AppConfig;
import app.Servent;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.MessageUtil;
import servent.snapshot.CausalBroadcastShared;
import servent.snapshot.SnapshotCollector;

public class TellHandler implements Runnable {

    private Message message;
    private SnapshotCollector collector;

    public TellHandler(Message message, SnapshotCollector collector) {
        this.message = message;
        this.collector = collector;
    }

    @Override
    public void run() {
        if (message.getType() == MessageType.TELL) {
            Servent sender = CausalBroadcastShared.getAskSender();

            if (sender.equals(AppConfig.LOCAL_SERVENT)) {
                collector.addSnapshot(message.getSender(), Integer.parseInt(message.getText()));
            } else {
                AppConfig.print(String.format("Redirecting TELL from %s to %s", message.getSender(), sender));
                MessageUtil.sendMessage(message.setReceiver(sender).setSender());
            }
        }
    }
}
