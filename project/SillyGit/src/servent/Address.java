package servent;

import java.io.Serializable;
import java.util.Objects;

public class Address implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String host;
    private final int port;

    public Address(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Address) {
            Address a = (Address) obj;
            return getHost().equals(a.getHost()) && getPort() == a.getPort();
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getHost(), getPort());
    }

    @Override
    public String toString() {
        return getHost() + ":" + getPort();
    }
}
