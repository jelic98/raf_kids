package data;

import java.math.BigInteger;

public class Key extends Field<BigInteger> {

    private static final long serialVersionUID = 1L;

    public Key(Object field) {
        super(Hash.get(field));
    }
}
