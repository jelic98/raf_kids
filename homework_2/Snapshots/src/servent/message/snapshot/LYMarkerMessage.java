package servent.message.snapshot;

import app.ServentInfo;
import servent.message.BasicMessage;
import servent.message.MessageType;

public class LYMarkerMessage extends BasicMessage {

    private static final long serialVersionUID = 1L;

    public LYMarkerMessage(ServentInfo sender, ServentInfo receiver, int collectorId) {
        super(MessageType.LY_MARKER, sender, receiver, String.valueOf(collectorId));
    }
}
