package message;

import app.App;
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
        // TODO Handle newest file version (-1)
        // TODO What is file already exist?
        getData().save(Config.STORAGE_PATH);
        Config.STORAGE.add(getData());
        App.print(String.format("File %s added to storage", getData()));
    }

    @Override
    public String toString() {
        return super.toString() + " with data " + getData();
    }

    public FileData getData() {
        return data;
    }
}

