package constant;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

public class CloudinaryConfig {
    private static final Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
        "cloud_name", "dtuq",
        "api_key", "439845824665539",
        "api_secret", "g6mX_F7X7csy_M8gu8pI18nZ61s",
        "secure", true 
    ));

    public static Cloudinary getInstance() {
        return cloudinary;
    }
}
