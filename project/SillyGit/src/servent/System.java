package servent;

import app.App;
import app.Config;
import data.Key;
import data.Value;
import file.FileData;
import message.Message;
import message.SorryMessage;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class System {

    public Set<Servent> activeServents;

    public System() {
        activeServents = Collections.newSetFromMap(new ConcurrentHashMap<>());
    }

    public Servent[] getServents(Key key) {
        if (activeServents.isEmpty()) {
            return null;
        }

        int maxIndex = Math.min(Config.K, activeServents.size());

        List<Servent> servents = new ArrayList<>(activeServents);
        servents.sort(new Comparator<Servent>() {
            @Override
            public int compare(Servent s1, Servent s2) {
                int d1 = key.get() ^ s1.hashCode();
                int d2 = key.get() ^ s2.hashCode();

                return Math.abs(d1 - d2);
            }
        });

        Servent[] result = new Servent[maxIndex];
        servents.subList(0, maxIndex).toArray(result);

        return result;
    }

    public void addServent(Servent servent) {
        if (!activeServents.add(servent)) {
            App.send(new SorryMessage(servent));
        }
    }

    public void broadcast(Message message) {
        if (Config.LOCAL.equals(Config.BOOTSTRAP)) {
            for (Servent servent : activeServents) {
                App.send(message.redirect(servent));
            }
        }
    }
}
