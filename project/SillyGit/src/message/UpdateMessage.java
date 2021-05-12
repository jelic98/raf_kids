package message;

import app.Address;
import app.App;
import app.Config;
import app.Servent;

import java.util.*;

public class UpdateMessage extends Message {

    private static final long serialVersionUID = 1L;

    private final Set<Address> addresses;

    public UpdateMessage() {
        super(null, Config.LOCAL, Config.CHORD.getNextServent());

        addresses = new HashSet<>();
    }

    public UpdateMessage(UpdateMessage m) {
        super(m);

        addresses = m.addresses;
    }

    @Override
    protected Message copy() {
        return new UpdateMessage(this);
    }

    @Override
    protected void handle() {
        if (getSender().equals(Config.LOCAL)) {
            for (Address address : getAddresses()) {
                Config.CHORD.addServents(new Servent(address));
            }
        } else {
            Config.CHORD.addServents(getSender());
            getAddresses().add(Config.LOCAL.getAddress());
            App.send(redirect(Config.CHORD.getNextServent()));
        }
    }

    @Override
    public String toString() {
        return super.toString() + " with addresses " + getAddresses();
    }

    public Set<Address> getAddresses() {
        return addresses;
    }
}
