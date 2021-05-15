package servent;

import app.App;
import app.Config;
import file.Key;
import message.CheckAskMessage;
import message.FailMessage;
import message.PingAskMessage;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class System {

    private Set<Servent> servents;
    private Set<Servent> pinged;
    private Set<Servent> checked;

    public System() {
        servents = Collections.newSetFromMap(new ConcurrentHashMap<>());
        pinged = Collections.newSetFromMap(new ConcurrentHashMap<>());
        checked = Collections.newSetFromMap(new ConcurrentHashMap<>());
    }

    public Servent[] getServents(Key key, boolean inclusive) {
        if (servents.isEmpty()) {
            return new Servent[0];
        }

        int maxIndex = Math.min(Config.K, servents.size() + (inclusive ? 1 : 0));

        List<Servent> servents = new ArrayList<>(this.servents);

        if (inclusive) {
            servents.add(Config.LOCAL);
        }

        servents.sort(new Comparator<Servent>() {
            @Override
            public int compare(Servent s1, Servent s2) {
                BigInteger d1 = key.get().xor(s1.getKey().get());
                BigInteger d2 = key.get().xor(s2.getKey().get());

                return d1.compareTo(d2);
            }
        });

        Servent[] result = new Servent[maxIndex];
        servents.subList(0, maxIndex).toArray(result);

        return result;
    }

    public Servent[] getServents(Key key) {
        return getServents(key, true);
    }

    public boolean addServent(Servent servent) {
        return servents.add(servent);
    }

    public boolean removeServent(Servent servent) {
        return servents.remove(servent);
    }

    public void ping() {
        pinged.clear();
        pinged.addAll(servents);

        for (Servent servent : servents) {
            App.send(new PingAskMessage(servent, false));
        }

        try {
            Thread.sleep(Config.FAILURE_SOFT);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        Set<Servent> active = new HashSet<>(servents);
        active.removeAll(pinged);

        for (Servent servent : active) {
            for (Servent pinged : pinged) {
                App.send(new CheckAskMessage(servent, pinged));
            }
        }

        try {
            Thread.sleep(Config.FAILURE_HARD);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        for (Servent servent : pinged) {
            App.send(new FailMessage(servent));
            Config.SYSTEM.removeServent(servent);
        }
    }

    public void pong(Servent servent) {
        pinged.remove(servent);
    }

    public void check(Servent servent) {
        checked.add(servent);

        App.send(new PingAskMessage(servent, true));

        try {
            Thread.sleep(Config.FAILURE_SOFT);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void uncheck(Servent servent) {
        checked.remove(servent);
    }

    public boolean isChecked(Servent servent) {
        return checked.contains(servent);
    }
}
