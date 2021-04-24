package servent.handler;

import app.AppConfig;
import app.Servent;
import servent.message.MessageUtil;
import servent.message.TellMessage;
import servent.snapshot.BroadcastShared;
import servent.snapshot.SnapshotCollector;

public class TellHandler implements Runnable {

    private final TellMessage message;
    private final SnapshotCollector collector;

    public TellHandler(TellMessage message, SnapshotCollector collector) {
        this.message = message;
        this.collector = collector;
    }

    @Override
    public void run() {
        Servent sender = BroadcastShared.getAskSender();

        if (sender.equals(AppConfig.LOCAL_SERVENT)) {
            collector.addSnapshot(message.getSender(), message.getSnapshot());
        } else {
            AppConfig.print(String.format("Redirecting TELL from %s to %s", message.getSender(), sender));
            MessageUtil.sendMessage(message.setReceiver(sender).setSender());
        }
    }
}
