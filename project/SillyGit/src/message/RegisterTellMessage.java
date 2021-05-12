package message;

import app.App;
import app.Config;
import app.Servent;
import data.Key;
import data.Value;

import java.util.Map;

public class RegisterTellMessage extends Message {

    private static final long serialVersionUID = 1L;

    private final Map<Key, Value> chunk;

    public RegisterTellMessage(Servent receiver, Map<Key, Value> chunk) {
        super(null, Config.LOCAL, receiver);

        this.chunk = chunk;
    }

    public RegisterTellMessage(RegisterTellMessage m) {
        super(m);

        chunk = m.chunk;
    }

    @Override
    protected Message copy() {
        return new RegisterTellMessage(this);
    }

    @Override
    protected void handle() {
        Config.CHORD.initialize(getSender(), getChunk());

        App.send(new PublishMessage());
        App.send(new UpdateMessage());
    }

    @Override
    public String toString() {
        return super.toString() + " with values " + getChunk();
    }

    public Map<Key, Value> getChunk() {
        return chunk;
    }
}
