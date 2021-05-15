package servent;

import file.Key;

import java.io.Serializable;
import java.util.Objects;

public class Servent implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Address address;
    private final String team;
    private final int id;

    public Servent(String host, int port, String team, int id) {
        this.team = team;
        this.id = id;

        address = new Address(host, port);
    }

    public Address getAddress() {
        return address;
    }

    public String getTeam() {
        return team;
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
