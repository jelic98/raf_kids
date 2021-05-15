package servent;

import file.Key;

import java.io.Serializable;
import java.util.Objects;

public class Servent implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Address address;
    private final int id;

    public Servent(String host, int port, int id) {
        this.id = id;

        address = new Address(host, port);
    }

    public Address getAddress() {
        return address;
    }

    public int getId() {
        return id;
    }

    public Key getKey() {
        return new Key(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Servent) {
            Servent s = (Servent) obj;
            return getAddress().equals(s.getAddress());
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAddress());
    }

    @Override
    public String toString() {
        return getAddress().toString();
    }
}
