package servent.message;

import app.ServentInfo;

import java.math.BigInteger;

public class SumHelpMessage extends BasicMessage {

    private static final long serialVersionUID = -1675414479988118253L;

    public SumHelpMessage(ServentInfo sender, ServentInfo receiver, BigInteger mid) {
        super(MessageType.SUM_HELP_MESSAGE, sender, receiver, mid.toString());
    }
}
