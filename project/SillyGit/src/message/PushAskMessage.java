package message;

import app.App;
import app.Config;
import file.FileData;
import servent.Servent;

public class PushAskMessage extends Message {

    private static final long serialVersionUID = 1L;

    private final FileData data;

    public PushAskMessage(Servent receiver, FileData data) {
        super(null, Config.LOCAL, receiver);

        this.data = data;
    }

    public PushAskMessage(PushAskMessage m) {
        super(m);

        data = m.data;
    }

    @Override
    protected Message copy() {
        return new PushAskMessage(this);
    }

    @Override
    protected void handle() {
        Servent servent = Config.NETWORK.getServent(getData().getKey());

        if (servent.equals(Config.LOCAL)) {
            if (Config.STORAGE.contains(getData())) {
                FileData data = Config.STORAGE.get(getData());

                boolean conflict = data.getVersion() >= getData().getVersion();

                if (!conflict) {
                    getData().save(Config.STORAGE_PATH);
                    Config.STORAGE.add(getData());
                }

                App.send(new PushTellMessage(getSender(), getData(), data, conflict));
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

