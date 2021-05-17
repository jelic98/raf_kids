package message;

import app.App;
import app.Config;
import file.FileData;
import servent.Servent;

public class RemoveMessage extends Message {

    private static final long serialVersionUID = 1L;

    private final FileData data;

    public RemoveMessage(Servent receiver, FileData data) {
        super(null, Config.LOCAL, receiver);

        this.data = data;
    }

    public RemoveMessage(RemoveMessage m) {
        super(m);

        data = m.data;
    }

    @Override
    protected Message copy() {
        return new RemoveMessage(this);
    }

    @Override
    protected void handle() {
        Servent servent = Config.NETWORK.getServent(getData().getKey());

        if (servent.equals(Config.LOCAL)) {
            if (Config.STORAGE.contains(getData())) {
                Config.STORAGE.remove(getData());
                App.print(String.format("File %s removed from storage", getData()));
            } else {
                App.print(String.format("File %s not found", getData()));
            }
        } else {
            if (!containsSender(servent)) {
                App.send(redirect(servent));
            }
        }
    }

    @Override
    public String toString() {
        return super.toString() + " with data " + getData();
    }

    public FileData getData() {
        return data;
    }
}

