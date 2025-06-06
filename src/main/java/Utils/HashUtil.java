
package Utils;

import java.security.MessageDigest;

public class HashUtil {
    public static byte[] hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return md.digest(password.getBytes("UTF-8")); // Trả về byte[] 
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}