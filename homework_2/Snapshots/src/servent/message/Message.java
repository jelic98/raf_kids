package servent.message;

import app.Servent;

import java.io.Serializable;

public interface Message extends Serializable {

    MessageType getType();

    String getText();

    Servent getSender();

    Message setSender();

    Servent getLastSender();

    boolean containsSender(Servent sender);

    Servent getReceiver();

    Message setReceiver(Servent receiver);

    void sendEffect();
}
