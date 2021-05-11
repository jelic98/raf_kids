package data;

import java.io.Serializable;
import java.util.Objects;

class Field<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private final T field;

    public Field(T field) {
        this.field = field;
    }

    public T get() {
        return field;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Field) {
            Field d = (Field) obj;
            return get() == d.get();
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(get());
    }

    @Override
    public String toString() {
        return String.valueOf(get());
    }
}
