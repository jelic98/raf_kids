package servent.handler;

import servent.message.Message;
import servent.message.MessageType;
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
            snapshotCollector.addSnapshot(clientMessage.getOriginalSenderInfo().getId(),
                    Integer.parseInt(clientMessage.getMessageText()));
        }
    }
}
