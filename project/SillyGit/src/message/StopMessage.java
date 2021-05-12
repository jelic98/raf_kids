package message;

import app.App;
import app.Config;
import app.ServentSingle;

public class StopMessage extends Message {

    private static final long serialVersionUID = 1L;

    public StopMessage() {
        super(null, Config.LOCAL_SERVENT, Config.BOOTSTRAP_SERVER);
    }

    public StopMessage(StopMessage m) {
        super(m);
    }

    @Override
    protected Message copy() {
        return new StopMessage(this);
    }

    @Override
    protected void handle(MessageHandler handler) {
        if (Config.LOCAL_SERVENT.equals(Config.BOOTSTRAP_SERVER)) {
            Config.CHORD.broadcast(this);
        }

        handler.stop();
        ServentSingle.stop();
    }
}
