package data;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hash {

    public static BigInteger get(Object object) {
        byte[] bytes = BigInteger.valueOf(object.hashCode()).toByteArray();

        try {
            return new BigInteger(MessageDigest.getInstance("SHA-1").digest(bytes));
        }catch (NoSuchAlgorithmException e) {
            return BigInteger.valueOf(0);
        }
    }
}
