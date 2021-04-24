package servent.message;

import app.ServentInfo;
import java.util.List;
import java.util.Map;

public class AskMessage extends CausalBroadcastMessage {

    private static final long serialVersionUID = 1L;

    public AskMessage(ServentInfo originalSenderInfo, ServentInfo receiverInfo, Map<Integer, Integer> senderVectorClock) {
        super(MessageType.ASK, originalSenderInfo, receiverInfo, null, senderVectorClock);
    }

    private AskMessage(ServentInfo originalSenderInfo, ServentInfo receiverInfo, List<ServentInfo> routeList,
                       int messageId, Map<Integer, Integer> senderVectorClock) {
        super(MessageType.ASK, originalSenderInfo, receiverInfo, routeList, null, messageId, senderVectorClock);
    }

    @Override
    protected Message createInstance(MessageType type, ServentInfo originalSenderInfo, ServentInfo receiverInfo, List<ServentInfo> routeList, String messageText, int messageId) {
        return new AskMessage(originalSenderInfo, receiverInfo, routeList, messageId, getSenderVectorClock());
    }
}
