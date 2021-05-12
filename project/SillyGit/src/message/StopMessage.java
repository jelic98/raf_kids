package message;

import app.App;
import app.Config;
import app.Servent;
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
    protected void handle(MessageHandler handler) {
        if (Config.LOCAL.equals(Config.BOOTSTRAP)) {
            for (Servent servent : Config.ACTIVE_SERVENTS) {
                App.send(redirect(servent));
            }
        }

        handler.stop();
        ServentSingle.stop();
    }
}
