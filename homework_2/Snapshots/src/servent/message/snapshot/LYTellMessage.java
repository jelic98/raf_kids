package servent.message.snapshot;

import app.ServentInfo;
import app.snapshot_bitcake.LYSnapshotResult;
import servent.message.BasicMessage;
import servent.message.Message;
import servent.message.MessageType;

import java.util.List;

public class LYTellMessage extends BasicMessage {

    private static final long serialVersionUID = 1L;

    private final LYSnapshotResult lySnapshotResult;

    public LYTellMessage(ServentInfo sender, ServentInfo receiver, LYSnapshotResult lySnapshotResult) {
        super(MessageType.LY_TELL, sender, receiver);

        this.lySnapshotResult = lySnapshotResult;
    }

    private LYTellMessage(MessageType messageType, ServentInfo sender, ServentInfo receiver,
                          boolean white, List<ServentInfo> routeList, String messageText, int messageId,
                          LYSnapshotResult lySnapshotResult) {
        super(messageType, sender, receiver, white, routeList, messageText, messageId);
        this.lySnapshotResult = lySnapshotResult;
    }

    public LYSnapshotResult getLYSnapshotResult() {
        return lySnapshotResult;
    }

    @Override
    public Message setRedColor() {
        return new LYTellMessage(getMessageType(), getOriginalSenderInfo(), getReceiverInfo(),
                false, getRoute(), getMessageText(), getMessageId(), getLYSnapshotResult());
    }
}
