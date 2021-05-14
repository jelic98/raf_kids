package message;

import app.App;
import app.Config;
import file.FileData;
import servent.Servent;

public class PushForceMessage extends Message {

    private static final long serialVersionUID = 1L;

    private FileData data;

    public PushForceMessage(Servent receiver, FileData data) {
        super(null, Config.LOCAL, receiver);

        this.data = data;
    }

    public PushForceMessage(PushForceMessage m) {
        super(m);

        data = m.data;
    }

    @Override
    protected Message copy() {
        return new PushForceMessage(this);
    }

    @Override
    protected void handle() {
        getData().save(Config.STORAGE_PATH);
        Config.STORAGE.add(getData());
        App.print(String.format("File %s force pushed to storage", getData()));
    }

    @Override
    public String toString() {
        return super.toString() + " with data " + getData();
    }

    public FileData getData() {
        return data;
    }
}

