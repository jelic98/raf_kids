package servent.handler;

import app.CausalBroadcastShared;
import servent.message.Message;
import servent.message.MessageType;

/**
 * Handles the CAUSAL_BROADCAST message. Fairly simple, as we assume that we are
 * in a clique. We add the message to a pending queue, and let the check on the queue
 * take care of the rest.
 * @author bmilojkovic
 *
 */
public class CausalBroadcastHandler implements MessageHandler {

	private Message clientMessage;
	
	public CausalBroadcastHandler(Message clientMessage) {
		this.clientMessage = clientMessage;
	}
	
	@Override
	public void run() {
		//Sanity check.
		if (clientMessage.getMessageType() == MessageType.CAUSAL_BROADCAST) {
			/*
			 * Same print as the one in BROADCAST handler.
			 * Kind of useless here, as we assume a clique.
			 */
			
			/*
			ServentInfo senderInfo = clientMessage.getOriginalSenderInfo();
			ServentInfo lastSenderInfo = clientMessage.getRoute().size() == 0 ?
					clientMessage.getOriginalSenderInfo() :
					clientMessage.getRoute().get(clientMessage.getRoute().size()-1);
			
			String text = String.format("Got %s from %s causally broadcast by %s\n",
					clientMessage.getMessageText(), lastSenderInfo, senderInfo);
			AppConfig.timestampedStandardPrint(text);
			*/
			
			/* 
			 * Uncomment the next line and comment out the two afterwards
			 * to see what happens when causality is broken.
			 */
//			CausalBroadcastShared.commitCausalMessage(clientMessage);
			
			CausalBroadcastShared.addPendingMessage(clientMessage);
			CausalBroadcastShared.checkPendingMessages();
		}
	}

}
