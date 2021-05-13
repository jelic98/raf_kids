package servent;

import java.io.Serializable;
import java.util.Objects;

public class Servent implements Serializable {

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
