package servent.handler;

import app.AppConfig;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.MessageUtil;
import servent.message.SumResultMessage;

import java.math.BigInteger;

public class SumHelpHandler implements MessageHandler {

    private final Message clientMessage;

    public SumHelpHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

    @Override
    public void run() {
        if (clientMessage.getMessageType() == MessageType.SUM_HELP_MESSAGE) {

            BigInteger sum = BigInteger.ZERO;
            BigInteger mid = new BigInteger(clientMessage.getMessageText());

            for (BigInteger counter = BigInteger.ONE; counter.compareTo(mid) == -1;
                 counter = counter.add(BigInteger.ONE)) {
                sum = sum.add(counter);
            }

            MessageUtil.sendMessage(new SumResultMessage(
                    AppConfig.myServentInfo, clientMessage.getOriginalSenderInfo(), sum));
        }

    }

}
