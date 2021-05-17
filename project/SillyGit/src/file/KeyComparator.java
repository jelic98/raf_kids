package file;

import servent.Servent;

import java.math.BigInteger;
import java.util.Comparator;

public class KeyComparator implements Comparator<Servent> {

    private Key key;

    public KeyComparator(Key key) {
        this.key = key;
    }

    @Override
    public int compare(Servent s1, Servent s2) {
        BigInteger d1 = key.get().xor(s1.getKey().get());
        BigInteger d2 = key.get().xor(s2.getKey().get());

        return d1.compareTo(d2);
    }
}
