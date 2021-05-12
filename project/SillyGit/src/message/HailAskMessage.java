package message;

import app.Address;
import app.App;
import app.Config;
import app.Servent;

import java.util.Random;

public class HailAskMessage extends Message {

    private static final long serialVersionUID = 1L;
    private static final Random random = new Random(System.currentTimeMillis());

    private final Address address;

    public HailAskMessage() {
        super(null, Config.LOCAL, Config.BOOTSTRAP);

        address = Config.LOCAL.getAddress();
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
    protected void handle() {
        Servent servent = null;

        if (!Config.ACTIVE_SERVENTS.isEmpty()) {
            int index = random.nextInt(Config.ACTIVE_SERVENTS.size());
            servent = Config.ACTIVE_SERVENTS.get(index);
        }

        App.send(new HailTellMessage(getSender(), servent));
    }

    @Override
    public String toString() {
        return super.toString() + " with address " + getAddress();
    }

    public Address getAddress() {
        return address;
    }
}

