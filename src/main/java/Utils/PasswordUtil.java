
package Utils;

import java.nio.charset.StandardCharsets;
import java.util.Random;


public class PasswordUtil {
    public static byte[] generateRandomPassword() {
        int randomNum = 10000 + new Random().nextInt(90000); // số ngẫu nhiên 5 chữ số
        String passwordStr = String.valueOf(randomNum);
        return passwordStr.getBytes(StandardCharsets.UTF_8);
    }

}
