package Utils;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import constant.CloudinaryConfig;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class CloudinaryUploader {
    public static String upload(InputStream inputStream, String contentType, String folder) throws IOException {

        byte[] bytes = inputStream.readAllBytes(); // Java 9+

        Cloudinary cloudinary = CloudinaryConfig.getInstance();

        Map uploadResult = cloudinary.uploader().upload(
            bytes,
            ObjectUtils.asMap(
                "resource_type", "auto",
                "folder", folder
            )
        );

        return uploadResult.get("secure_url").toString();
    }
}

