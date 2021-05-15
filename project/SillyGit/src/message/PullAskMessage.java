package message;

import app.App;
import app.Config;
import file.FileData;
import servent.Servent;

public class PullAskMessage extends Message {

    private static final long serialVersionUID = 1L;

    private FileData data;

    public PullAskMessage(Servent receiver, FileData data) {
        super(null, Config.LOCAL, receiver);

        this.data = data;
    }

    public PullAskMessage(PullAskMessage m) {
        super(m);

        data = m.data;
    }

    @Override
    protected Message copy() {
        return new PullAskMessage(this);
    }

    @Override
    protected void handle() {
        Servent[] servents = Config.SYSTEM.getServents(getData().getKey());

        if (servents[0].equals(Config.LOCAL)) {
            if (Config.STORAGE.contains(getData())) {
                FileData data = Config.STORAGE.get(getData());

                data.load(Config.STORAGE_PATH);
                App.send(new PullTellMessage(getSender(), data));
            } else {
                App.print(String.format("File %s not found", getData()));
            }
        } else {
            if (containsSender(servents[0])) {
                App.send(redirect(servents[0]));
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

