package message;

import app.App;
import app.Config;
import file.FileData;
import servent.Servent;

public class ReplicateMessage extends Message {

    private static final long serialVersionUID = 1L;

    private final FileData data;
    private final boolean background;

    public ReplicateMessage(Servent receiver, FileData data, boolean background) {
        super(null, Config.LOCAL, receiver);

        this.data = new FileData(data);
        this.background = background;
    }

    public ReplicateMessage(ReplicateMessage m) {
        super(m);

        data = m.data;
        background = m.background;
    }

    @Override
    public boolean shouldPrint() {
        return !isBackground();
    }

    @Override
    protected Message copy() {
        return new ReplicateMessage(this);
    }

    @Override
    protected void handle() {
        Servent servent = Config.NETWORK.getServent(getData().getKey());

        if (servent.equals(Config.LOCAL)) {
            getData().save(Config.STORAGE_PATH);
            Config.STORAGE.add(getData());
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

    public boolean isBackground() {
        return background;
    }
}

