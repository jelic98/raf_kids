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

    public CausalBroadcastMessage(ServentInfo originalSenderInfo, ServentInfo receiverInfo,
                                  String messageText, Map<Integer, Integer> senderVectorClock) {
        this(MessageType.CAUSAL_BROADCAST, originalSenderInfo, receiverInfo, messageText, senderVectorClock);
    }

    protected CausalBroadcastMessage(MessageType messageType, ServentInfo originalSenderInfo, ServentInfo receiverInfo,
                                     String messageText, Map<Integer, Integer> senderVectorClock) {
        super(messageType, originalSenderInfo, receiverInfo, messageText);

        this.senderVectorClock = senderVectorClock;
    }

    private CausalBroadcastMessage(ServentInfo originalSenderInfo, ServentInfo receiverInfo,
                                   List<ServentInfo> routeList, String messageText, int messageId,
                                   Map<Integer, Integer> senderVectorClock) {
        this(MessageType.CAUSAL_BROADCAST, originalSenderInfo, receiverInfo, routeList, messageText, messageId, senderVectorClock);
    }

    protected CausalBroadcastMessage(MessageType messageType, ServentInfo originalSenderInfo, ServentInfo receiverInfo,
                                     List<ServentInfo> routeList, String messageText, int messageId,
                                     Map<Integer, Integer> senderVectorClock) {
        super(messageType, originalSenderInfo, receiverInfo, routeList, messageText, messageId);

        this.senderVectorClock = senderVectorClock;
    }

    @Override
    protected Message createInstance(MessageType type, ServentInfo originalSenderInfo, ServentInfo receiverInfo, List<ServentInfo> routeList, String messageText, int messageId) {
        return new CausalBroadcastMessage(originalSenderInfo, receiverInfo, routeList, messageText, messageId, senderVectorClock);
    }

    public Map<Integer, Integer> getSenderVectorClock() {
        return senderVectorClock;
    }
}
