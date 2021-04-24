package servent.handler;

import app.AppConfig;
import servent.message.*;
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
        if (clientMessage.getMessageType() == MessageType.TELL) {
            int askSender = CausalBroadcastShared.getAskSender();

            if(askSender == AppConfig.myServentInfo.getId()) {
                snapshotCollector.addSnapshot(clientMessage.getOriginalSenderInfo().getId(),
                        Integer.parseInt(clientMessage.getMessageText()));
            }else {
                AppConfig.timestampedStandardPrint(String.format("Redirecting TELL from %d to %d",
                        clientMessage.getOriginalSenderInfo().getId(), askSender));
                MessageUtil.sendMessage(clientMessage.changeReceiver(askSender).makeMeASender());
            }
        }
    }
}
