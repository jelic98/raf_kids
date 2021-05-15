package file;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Key extends Field<BigInteger> {

    private static final long serialVersionUID = 1L;

    public Key(Object field) {
        super(hash(field));
    }

    private static BigInteger hash(Object object) {
        byte[] bytes = BigInteger.valueOf(object.hashCode()).toByteArray();

        try {
            return new BigInteger(MessageDigest.getInstance("SHA-1").digest(bytes));
        } catch (NoSuchAlgorithmException e) {
            return BigInteger.valueOf(0);
        }
    }
}
