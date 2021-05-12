package message;

import app.App;
import app.Config;
import app.Servent;
import data.Key;
import data.Value;

import java.util.HashMap;
import java.util.Map;

public class RegisterAskMessage extends Message {

    private static final long serialVersionUID = 1L;

    public RegisterAskMessage(Servent receiver) {
        super(null, Config.LOCAL_SERVENT, receiver);
    }

    public RegisterAskMessage(RegisterAskMessage m) {
        super(m);
    }

    @Override
    protected Message copy() {
        return new RegisterAskMessage(this);
    }

    @Override
    protected void handle(MessageHandler handler) {
        Servent sender = getSender();

        if (Config.CHORD.isCollision(sender.getChordId())) {
            App.send(new SorryMessage(getSender()));
            return;
        }

        Key serventId = new Key(sender.getChordId());

        if (Config.CHORD.containsKey(serventId)) {
            Map<Key, Value> chunk = new HashMap<>();
            transferChunk(Config.CHORD.getChunk(), chunk, sender);
            App.send(new RegisterTellMessage(sender, chunk));
        } else {
            App.send(redirect(Config.CHORD.getServent(serventId)));
        }
    }

    private void transferChunk(Map<Key, Value> from, Map<Key, Value> to, Servent sender) {
        Servent predecessor = Config.CHORD.getPredecessor();

        if (predecessor == null) {
            predecessor = Config.LOCAL_SERVENT;
        }

        Config.CHORD.setPredecessor(sender);

        int myId = Config.LOCAL_SERVENT.getChordId();
        int hisPredId = predecessor.getChordId();
        int newNodeId = sender.getChordId();

        for (Map.Entry<Key, Value> valueEntry : from.entrySet()) {
            int key = valueEntry.getKey().get();

            if (hisPredId == myId) {
                if (myId < newNodeId) {
                    if (key <= newNodeId && key > myId) {
                        to.put(valueEntry.getKey(), valueEntry.getValue());
                    }
                } else {
                    if (key <= newNodeId || key > myId) {
                        to.put(valueEntry.getKey(), valueEntry.getValue());
                    }
                }
            } else if (hisPredId < myId) {
                if (key <= newNodeId) {
                    to.put(valueEntry.getKey(), valueEntry.getValue());
                }
            } else {
                if (hisPredId > newNodeId) {
                    if (key <= newNodeId || key > hisPredId) {
                        to.put(valueEntry.getKey(), valueEntry.getValue());
                    }
                } else {
                    if (key <= newNodeId && key > hisPredId) {
                        to.put(valueEntry.getKey(), valueEntry.getValue());
                    }
                }
            }
        }

        for (Key key : to.keySet()) {
            from.remove(key);
        }

        Config.CHORD.setChunk(from);
    }
}

