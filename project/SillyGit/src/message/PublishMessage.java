package message;

import app.Address;
import app.Config;
import app.Servent;

public class PublishMessage extends Message {

    private static final long serialVersionUID = 1L;

    private final Address address;

    public PublishMessage() {
        super(null, Config.LOCAL_SERVENT, Config.BOOTSTRAP_SERVER);

        this.address = Config.LOCAL_SERVENT.getAddress();
    }

    public PublishMessage(PublishMessage m) {
        super(m);

        address = m.address;
    }

    @Override
    protected Message copy() {
        return new PublishMessage(this);
    }

    @Override
    protected void handle(MessageHandler handler) {
        Config.BOOTSTRAP_SERVENTS.add(new Servent(getAddress()));
    }

    @Override
    public String toString() {
        return super.toString() + " with address " + getAddress();
    }

    public Address getAddress() {
        return address;
    }
}
