package message;

import app.App;
import app.Config;
import command.CommandParser;
import command.Prompt;
import file.FileData;
import servent.Servent;

public class PushTellMessage extends Message {

    private static final long serialVersionUID = 1L;

    private FileData local;
    private FileData remote;
    private boolean conflict;

    public PushTellMessage(Servent receiver, FileData local, FileData remote, boolean conflict) {
        super(null, Config.LOCAL, receiver);

        this.local = local;
        this.remote = remote;
        this.conflict = conflict;
    }

    public PushTellMessage(PushTellMessage m) {
        super(m);

        local = m.local;
        remote = m.remote;
        conflict = m.conflict;
    }

    @Override
    protected Message copy() {
        return new PushTellMessage(this);
    }

    @Override
    protected void handle() {
        if (isConflict()) {
            App.print(String.format("Cannot push file %s", getLocal()));
            showPrompt();
        }else {
            App.print(String.format("File %s pushed successfully", getLocal()));
        }
    }

    @Override
    public String toString() {
        return super.toString() + " with local " + getLocal() + " with remote " + getRemote();
    }

    public FileData getLocal() {
        return local;
    }

    public FileData getRemote() {
        return remote;
    }

    public boolean isConflict() {
        return conflict;
    }

    private void showPrompt() {
        CommandParser.addPrompt(new Prompt()
                .add(new Prompt.Option("view", new Prompt.Handler() {
                    @Override
                    public void handle() {
                        App.print(String.format("Local %s:\n%s", getLocal(), getLocal().getContent()));
                        App.print(String.format("Remote %s:\n%s", getRemote(), getRemote().getContent()));
                        showPrompt();
                    }
                }))
                .add(new Prompt.Option("pull", new Prompt.Handler() {
                    @Override
                    public void handle() {
                        getRemote().save(Config.WORKSPACE_PATH);
                        Config.WORKSPACE.add(getRemote());
                        App.print(String.format("File %s pulled to workspace", getRemote()));
                    }
                }))
                .add(new Prompt.Option("push", new Prompt.Handler() {
                    @Override
                    public void handle() {
                        App.send(new PushForceMessage(getSender(), getLocal()));
                    }
                })));
    }
}

