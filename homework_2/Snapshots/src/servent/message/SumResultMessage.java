package servent.message;

import app.ServentInfo;

import java.math.BigInteger;

public class SumResultMessage extends BasicMessage {

    private static final long serialVersionUID = -1121732534784647779L;

    public SumResultMessage(ServentInfo sender, ServentInfo receiver, BigInteger result) {
        super(MessageType.SUM_RESULT_MESSAGE, sender, receiver, result.toString());
    }
}
