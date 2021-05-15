package message;

import app.Config;
import file.FileData;
import servent.Servent;

public class RedirectMessage extends Message {

    private static final long serialVersionUID = 1L;

    private Servent servent;
    private FileData data;

    public RedirectMessage(Servent receiver, Servent servent, FileData data) {
        super(null, Config.LOCAL, receiver);

        this.servent = servent;
        this.data = data;
    }

    public RedirectMessage(RedirectMessage m) {
        super(m);

        servent = m.servent;
        data = m.data;
    }

    @Override
    protected Message copy() {
        return new RedirectMessage(this);
    }

    @Override
    protected void handle() {
        Config.WORKSPACE.addCached(getData(), getServent());
    }

    @Override
    public String toString() {
        return super.toString() + " with servent " + getServent() + " with data " + getData();
    }

    public Servent getServent() {
        return servent;
    }

    public FileData getData() {
        return data;
    }
}

