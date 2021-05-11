package message;

import app.App;
import app.Config;
import app.Servent;

import java.util.ArrayList;
import java.util.List;

public class UpdateMessage extends Message {

    private static final long serialVersionUID = 1L;

    private List<Integer> ports;

    public UpdateMessage(Servent receiver, List<Integer> ports) {
        super(Type.UPDATE, null, Config.LOCAL_SERVENT, receiver);

        this.ports = ports;
    }

    public UpdateMessage(Servent receiver) {
        this(receiver, new ArrayList<>());
    }

    public UpdateMessage(UpdateMessage m) {
        super(m);

        ports = m.ports;
    }

    @Override
    protected Message copy() {
        return new UpdateMessage(this);
    }

    @Override
    protected void handle(MessageHandler handler) {
        List<Servent> servents = new ArrayList<>();

        if (getSender().getPort() != Config.LOCAL_SERVENT.getPort()) {
            servents.add(new Servent(getSender().getPort()));

            List<Integer> ports = new ArrayList<>(getPorts());
            ports.add(Config.LOCAL_SERVENT.getPort());

            App.send(new UpdateMessage(Config.CHORD.getNextServent(), ports));
        } else {
            for (Integer port : getPorts()) {
                servents.add(new Servent(port));
            }
        }

        Config.CHORD.addServents(servents);
    }

    @Override
    public String toString() {
        return getType() + " with ports " + getPorts();
    }

    public List<Integer> getPorts() {
        return ports;
    }
}
