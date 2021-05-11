package data;

import java.io.Serializable;
import java.util.Objects;

public class Data implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Key key;
    private final Value value;

    public Data(Key key, Value value) {
        this.key = key;
        this.value = value;
    }

    public Key getKey() {
        return key;
    }

    public Value getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Data) {
            Data d = (Data) obj;
            return getKey() == d.getKey();
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKey());
    }

    @Override
    public String toString() {
        return getKey() + "->" + getValue();
    }
}
