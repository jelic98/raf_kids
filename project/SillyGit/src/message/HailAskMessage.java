package message;

import app.Address;
import app.App;
import app.Config;
import app.Servent;

import java.util.ArrayList;
import java.util.Random;

public class HailAskMessage extends Message {

    private static final long serialVersionUID = 1L;
    private static final Random random = new Random(System.currentTimeMillis());

    private Address address;

    public HailAskMessage(Servent receiver, Address address) {
        super(Type.HAIL_ASK, null, Config.LOCAL_SERVENT, receiver);

        this.address = address;
    }

    public HailAskMessage(HailAskMessage m) {
        super(m);

        address = m.address;
    }

    @Override
    protected Message copy() {
        return new HailAskMessage(this);
    }

    @Override
    protected void handle(MessageHandler handler) {
        Servent servent = null;

        if (Config.BOOTSTRAP_SERVENTS == null) {
            Config.BOOTSTRAP_SERVENTS = new ArrayList<>();
            Config.BOOTSTRAP_SERVENTS.add(getSender());
        } else {
            int index = random.nextInt(Config.BOOTSTRAP_SERVENTS.size());
            servent = Config.BOOTSTRAP_SERVENTS.get(index);
        }

        App.send(new HailTellMessage(getSender(), servent));
    }

    @Override
    public String toString() {
        return getType() + " with address " + getAddress();
    }

    public Address getAddress() {
        return address;
    }
}

