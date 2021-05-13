package message;

import app.Config;
import app.ServentSingle;

public class StopMessage extends Message {

    private static final long serialVersionUID = 1L;

    public StopMessage() {
        super(null, Config.LOCAL, Config.BOOTSTRAP);
    }

    public StopMessage(StopMessage m) {
        super(m);
    }

    @Override
    protected Message copy() {
        return new StopMessage(this);
    }

    @Override
    protected void handle() {
        Config.SYSTEM.broadcast(this);
        ServentSingle.stop();
    }
}
