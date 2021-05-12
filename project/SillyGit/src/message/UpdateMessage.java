package message;

import app.Address;
import app.App;
import app.Config;
import app.Servent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UpdateMessage extends Message {

    private static final long serialVersionUID = 1L;

    private final Set<Address> addresses;

    public UpdateMessage(Servent receiver, Set<Address> addresses) {
        super(null, Config.LOCAL_SERVENT, receiver);

        this.addresses = addresses;
    }

    public UpdateMessage(Servent receiver) {
        this(receiver, new HashSet<>());
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
    protected void handle(MessageHandler handler) {
        List<Servent> servents = new ArrayList<>();

        if (getSender().equals(Config.LOCAL_SERVENT)) {
            for (Address address : getAddresses()) {
                servents.add(new Servent(address));
            }
        } else {
            servents.add(getSender());

            if (addresses.add(Config.LOCAL_SERVENT.getAddress())) {
                App.send(redirect(Config.CHORD.getNextServent()));
            }
        }

        Config.CHORD.addServents(servents);
    }

    @Override
    public String toString() {
        return super.toString() + " with addresses " + getAddresses();
    }

    public Set<Address> getAddresses() {
        return addresses;
    }
}
