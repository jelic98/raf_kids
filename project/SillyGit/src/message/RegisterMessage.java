package message;

import app.App;
import app.Config;
import app.Servent;

import java.util.HashMap;
import java.util.Map;

public class RegisterMessage extends Message {

    private static final long serialVersionUID = 1L;

    public RegisterMessage(Servent receiver) {
        super(Type.REGISTER, null, Config.LOCAL_SERVENT, receiver);
    }

    public RegisterMessage(RegisterMessage m) {
        super(m);
    }

    @Override
    protected Message copy() {
        return new RegisterMessage(this);
    }

    @Override
    protected void handle(MessageHandler handler) {
        Servent servent = getSender();
        int port = servent.getPort();

        if (Config.CHORD.isCollision(servent.getChordId())) {
            App.send(new SorryMessage(getSender()));
            return;
        }

        if (Config.CHORD.isKeyMine(servent.getChordId())) {
            Servent hisPred = Config.CHORD.getPredecessor();
            if (hisPred == null) {
                hisPred = Config.LOCAL_SERVENT;
            }

            Config.CHORD.setPredecessor(servent);

            Map<Integer, Integer> myValues = Config.CHORD.getValueMap();
            Map<Integer, Integer> hisValues = new HashMap<>();

            int myId = Config.LOCAL_SERVENT.getChordId();
            int hisPredId = hisPred.getChordId();
            int newNodeId = servent.getChordId();

            for (Map.Entry<Integer, Integer> valueEntry : myValues.entrySet()) {
                if (hisPredId == myId) {
                    if (myId < newNodeId) {
                        if (valueEntry.getKey() <= newNodeId && valueEntry.getKey() > myId) {
                            hisValues.put(valueEntry.getKey(), valueEntry.getValue());
                        }
                    } else {
                        if (valueEntry.getKey() <= newNodeId || valueEntry.getKey() > myId) {
                            hisValues.put(valueEntry.getKey(), valueEntry.getValue());
                        }
                    }
                }
                if (hisPredId < myId) {
                    if (valueEntry.getKey() <= newNodeId) {
                        hisValues.put(valueEntry.getKey(), valueEntry.getValue());
                    }
                } else {
                    if (hisPredId > newNodeId) {
                        if (valueEntry.getKey() <= newNodeId || valueEntry.getKey() > hisPredId) {
                            hisValues.put(valueEntry.getKey(), valueEntry.getValue());
                        }
                    } else {
                        if (valueEntry.getKey() <= newNodeId && valueEntry.getKey() > hisPredId) {
                            hisValues.put(valueEntry.getKey(), valueEntry.getValue());
                        }
                    }
                }
            }

            for (Integer key : hisValues.keySet()) {
                myValues.remove(key);
            }

            Config.CHORD.setValueMap(myValues);

            App.send(new WelcomeAskMessage(Config.LOCAL_SERVENT, hisValues));
        } else {
            App.send(new RegisterMessage(Config.CHORD.getServent(servent.getChordId())));
        }
    }
}

