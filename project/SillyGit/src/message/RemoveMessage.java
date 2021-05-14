package message;

import app.App;
import app.Config;
import file.FileData;
import servent.Servent;

public class RemoveMessage extends Message {

    private static final long serialVersionUID = 1L;

    private FileData data;

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
        // TODO What is file does not exist?
        Config.STORAGE.remove(getData());
        App.print(String.format("File %s removed from storage", getData()));
    }

    @Override
    public String toString() {
        return super.toString() + " with key " + getData();
    }

    public FileData getData() {
        return data;
    }
}

