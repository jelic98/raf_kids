package servent.message;

import app.ServentInfo;

import java.util.List;
import java.util.Map;

/**
 * Has all the fancy stuff from {@link BasicMessage}, with an
 * added vector clock.
 *
 * @author bmilojkovic
 */
public class CausalBroadcastMessage extends BasicMessage {

    private static final long serialVersionUID = 1L;
    private final Map<Integer, Integer> senderVectorClock;

    public CausalBroadcastMessage(ServentInfo senderInfo, ServentInfo receiverInfo, String messageText,
                                  Map<Integer, Integer> senderVectorClock) {
        super(MessageType.CAUSAL_BROADCAST, senderInfo, receiverInfo, messageText);

        this.senderVectorClock = senderVectorClock;
    }

    public CausalBroadcastMessage(ServentInfo originalSenderInfo, ServentInfo receiverInfo, boolean isWhite,
                                  List<ServentInfo> routeList, String messageText, int messageId, Map<Integer, Integer> senderVectorClock) {
        super(MessageType.CAUSAL_BROADCAST, originalSenderInfo, receiverInfo, isWhite, routeList, messageText, messageId);

        this.senderVectorClock = senderVectorClock;
    }

    public Map<Integer, Integer> getSenderVectorClock() {
        return senderVectorClock;
    }

    @Override
    protected Message createInstance(MessageType type, ServentInfo originalSenderInfo, ServentInfo receiverInfo, boolean isWhite, List<ServentInfo> routeList, String messageText, int messageId) {
        return new CausalBroadcastMessage(originalSenderInfo, receiverInfo, isWhite, routeList, messageText, messageId, senderVectorClock);
    }
}
