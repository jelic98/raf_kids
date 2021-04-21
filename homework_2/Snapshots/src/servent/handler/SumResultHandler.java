package servent.handler;

import java.math.BigInteger;

import cli.command.SumCommand;
import servent.message.Message;
import servent.message.MessageType;

public class SumResultHandler implements MessageHandler {

	private final Message clientMessage;
	
	public SumResultHandler(Message clientMessage) {
		this.clientMessage = clientMessage;
	}
	
	@Override
	public void run() {
		if (clientMessage.getMessageType() == MessageType.SUM_RESULT_MESSAGE) {
			BigInteger result = new BigInteger(clientMessage.getMessageText());
			
			SumCommand.friendResult = result;
		}

	}

}
