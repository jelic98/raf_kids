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
        Servent[] servents = Config.SYSTEM.getServents(getData().getKey());

        if (servents[0] == Config.LOCAL) {
            if (Config.STORAGE.contains(getData())) {
                Config.STORAGE.remove(getData());
                App.print(String.format("File %s removed from storage", getData()));
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

