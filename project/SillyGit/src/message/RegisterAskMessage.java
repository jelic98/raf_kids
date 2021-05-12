package message;

import app.App;
import app.Config;
import servent.Servent;
import data.Key;

public class RegisterAskMessage extends Message {

    private static final long serialVersionUID = 1L;

    public RegisterAskMessage(Servent receiver) {
        super(null, Config.LOCAL, receiver);
    }

    public RegisterAskMessage(RegisterAskMessage m) {
        super(m);
    }

    @Override
    protected Message copy() {
        return new RegisterAskMessage(this);
    }

    @Override
    protected void handle() {
        Servent sender = getSender();
        Key senderKey = new Key(sender.hashCode());

        if (Config.SYSTEM.duplicateKey(senderKey)) {
            App.send(new SorryMessage(getSender()));
            return;
        }

        if (Config.SYSTEM.containsKey(senderKey)) {
            App.send(new RegisterTellMessage(sender, null));
        } else {
            App.send(redirect(Config.SYSTEM.getServent(senderKey)));
        }
    }
}

