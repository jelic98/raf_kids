package message;

import app.App;
import app.Config;
import file.FileData;
import servent.Servent;

public class PullTellMessage extends Message {

    private static final long serialVersionUID = 1L;

    private FileData data;

    public PullTellMessage(Servent receiver, FileData data) {
        super(null, Config.LOCAL, receiver);

        this.data = data;
    }

    public PullTellMessage(PullTellMessage m) {
        super(m);

        data = m.data;
    }

    @Override
    protected Message copy() {
        return new PullTellMessage(this);
    }

    @Override
    protected void handle() {
        getData().save(Config.WORKSPACE_PATH);
        Config.WORKSPACE.add(getData());
        App.print(String.format("File %s pulled to workspace", getData()));
    }

    @Override
    public String toString() {
        return super.toString() + " with data " + getData();
    }

    public FileData getData() {
        return data;
    }
}

