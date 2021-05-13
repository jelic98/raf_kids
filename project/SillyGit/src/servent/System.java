package servent;

import app.App;
import app.Config;
import data.Key;
import data.Value;
import message.Message;
import message.SorryMessage;

import java.util.*;

public class System {

    public Set<Servent> activeServents;

    public System() {
        activeServents = new HashSet<>();
    }

    public Servent[] getServents(Servent servent) {
        if (activeServents.isEmpty()) {
            return null;
        }

        int key = servent.hashCode();
        int maxIndex = Math.min(Config.K, activeServents.size());

        List<Servent> servents = new ArrayList<>(activeServents);
        servents.sort(new Comparator<Servent>() {
            @Override
            public int compare(Servent s1, Servent s2) {
                int d1 = key ^ s1.hashCode();
                int d2 = key ^ s2.hashCode();

                return Math.abs(d1 - d2);
            }
        });

        Servent[] result = new Servent[maxIndex];
        servents.subList(0, maxIndex).toArray(result);

        return result;
    }

    public void addServent(Servent servent) {
        if(!activeServents.add(servent)) {
            App.send(new SorryMessage(servent));
        }
    }

    public Value getValue(Key key) {
        return null;
    }

    public void putValue(Key key, Value value) {

    }

    public void broadcast(Message message) {
        if (Config.LOCAL.equals(Config.BOOTSTRAP)) {
            for (Servent servent : activeServents) {
                App.send(message.redirect(servent));
            }
        }
    }
}
