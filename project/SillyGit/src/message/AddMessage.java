package message;

import app.App;
import app.Config;
import file.FileData;
import servent.Servent;

public class AddMessage extends Message {

    private static final long serialVersionUID = 1L;

    private final FileData data;

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
        Servent servent = Config.NETWORK.getServent(getData().getKey());

        if (servent.equals(Config.LOCAL)) {
            if (Config.STORAGE.contains(getData())) {
                App.print(String.format("File %s already exists at %s", getData(), getReceiver()));
            } else {
                getData().save(Config.STORAGE_PATH);
                Config.STORAGE.add(getData());
                App.print(String.format("File %s added to storage at %s", getData(), getReceiver()));
            }
        } else {
            if (containsSender(servent)) {
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

