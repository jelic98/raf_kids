package app;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Servent implements Serializable {

    private static final long serialVersionUID = 1L;

    private final int id;
    private final String host;
    private final int port;
    private final int chordId;
    private final List<Servent> neighbors;

    public Servent(int id, String host, int port) {
        this.id = id;
        this.host = host;
        this.port = port;

        chordId = ChordState.chordHash(port);
        neighbors = new ArrayList<>();
    }

    public Servent(int port) {
        this(-1, "localhost", port);
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public int getChordId() {
        return chordId;
    }

    public List<Servent> getNeighbors() {
        return neighbors;
    }

    public boolean isNeighbor(Servent servent) {
        return neighbors.contains(servent);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Servent) {
            Servent s = (Servent) obj;
            return id == s.id;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }
}
