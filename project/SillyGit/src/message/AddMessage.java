package message;

import app.Config;
import file.FileData;
import servent.Servent;

public class AddMessage extends Message {

    private static final long serialVersionUID = 1L;

    private FileData data;

    public AddMessage(Servent receiver, FileData data) {
        super(null, Config.LOCAL, receiver);

        this.data = data;
    }

    public AddMessage(AddMessage m) {
        super(m);

        data = m.data;
    }

    @Override
    protected Message copy() {
        return new AddMessage(this);
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

