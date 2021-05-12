package app;

import java.io.Serializable;

public class Servent implements Serializable, Comparable<Servent> {

    private static final long serialVersionUID = 1L;

    private final Address address;

    public Servent(Address address) {
        this.address = address;
    }

    public Servent(String host, int port) {
        this(new Address(host, port));
    }

    public Address getAddress() {
        return address;
    }

    public int getChordId() {
        return hashCode() % Config.CHORD_SIZE;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Servent) {
            Servent s = (Servent) obj;
            return getAddress().equals(s.getAddress());
        }

        return false;
    }

    @Override
    public int hashCode() {
        return getAddress().hashCode();
    }

    @Override
    public String toString() {
        return getAddress().toString();
    }

    @Override
    public int compareTo(Servent s) {
        return Integer.compare(getChordId(), s.getChordId());
    }
}
