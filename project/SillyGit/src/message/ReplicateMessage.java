package message;

import app.App;
import app.Config;
import file.FileData;
import servent.Servent;

public class ReplicateMessage extends Message {

    private static final long serialVersionUID = 1L;

    private FileData data;

    public ReplicateMessage(Servent receiver, FileData data) {
        super(null, Config.LOCAL, receiver);

        this.data = data;
    }

    public ReplicateMessage(ReplicateMessage m) {
        super(m);

        data = m.data;
    }

    @Override
    public boolean shouldPrint() {
        return false;
    }

    @Override
    protected Message copy() {
        return new ReplicateMessage(this);
    }

    @Override
    protected void handle() {
        getData().save(Config.STORAGE_PATH);
        Config.STORAGE.add(getData());
    }

    @Override
    public String toString() {
        return super.toString() + " with data " + getData();
    }

    public FileData getData() {
        return data;
    }
}

