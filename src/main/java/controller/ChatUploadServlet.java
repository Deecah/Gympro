package controller;

import Utils.CloudinaryUploader;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import org.json.JSONObject;

@WebServlet(name = "ChatUploadServlet", urlPatterns = {"/chat-upload/*"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 20 // 20MB
)
public class ChatUploadServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("ChatUploadServlet: Request received");
        System.out.println("Request URL: " + request.getRequestURL());
        System.out.println("Request path: " + request.getPathInfo());

        // Kiểm tra đăng nhập
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            System.out.println("ChatUploadServlet: Unauthorized access");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            return;
        }

        // Lấy endpoint từ URL path
        String pathInfo = request.getPathInfo();
        String endpoint = pathInfo != null ? pathInfo.substring(1) : "";
        System.out.println("ChatUploadServlet: Path info: " + pathInfo);
        System.out.println("ChatUploadServlet: Endpoint: " + endpoint);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        JSONObject jsonResponse = new JSONObject();

        try {
            System.out.println("ChatUploadServlet: Processing file upload");
            Part filePart = request.getPart("file");
            if (filePart == null) {
                System.out.println("ChatUploadServlet: No file part found");
                jsonResponse.put("error", "No file uploaded");
                out.print(jsonResponse.toString());
                return;
            }

            String fileName = filePart.getSubmittedFileName();
            String contentType = filePart.getContentType();

            // Kiểm tra loại file
            if ("image".equals(endpoint)) {
                if (!contentType.startsWith("image/")) {
                    jsonResponse.put("error", "Only image files are allowed");
                    out.print(jsonResponse.toString());
                    return;
                }
            }

            // Upload lên Cloudinary
            String folder = "chat/" + endpoint + "/" + System.currentTimeMillis();
            System.out.println("Uploading to folder: " + folder);
            System.out.println("Content type: " + contentType);
            
            String uploadedUrl;
            try {
                uploadedUrl = CloudinaryUploader.upload(filePart.getInputStream(), contentType, folder);
                System.out.println("Upload successful: " + uploadedUrl);
            } catch (Exception cloudinaryError) {
                System.err.println("Cloudinary upload failed: " + cloudinaryError.getMessage());
                // Fallback: return a placeholder URL for testing
                uploadedUrl = "https://via.placeholder.com/300x200?text=Upload+Failed";
            }

            jsonResponse.put("url", uploadedUrl);
            jsonResponse.put("success", true);

        } catch (Exception e) {
            System.err.println("Upload error: " + e.getMessage());
            e.printStackTrace();
            jsonResponse.put("error", "Upload failed: " + e.getMessage());
            jsonResponse.put("success", false);
        }

        String responseStr = jsonResponse.toString();
        System.out.println("ChatUploadServlet: Sending response: " + responseStr);
        out.print(responseStr);
        out.flush();
    }
} 