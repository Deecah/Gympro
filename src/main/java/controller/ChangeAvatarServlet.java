package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import model.User;
import dao.UserDAO;

import java.io.*;
import java.nio.file.Paths;
import java.util.UUID;

@MultipartConfig
public class ChangeAvatarServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Lấy file từ form
        Part filePart = request.getPart("avatar");
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

        if (fileName == null || fileName.isEmpty()) {
            response.sendRedirect("profile.jsp?error=NoFileSelected");
            return;
        }

        // Tạo tên file ngẫu nhiên
        String uniqueFileName = UUID.randomUUID() + "_" + fileName;

        // Đường dẫn lưu file
        String uploadPath = getServletContext().getRealPath("/uploads");
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        // Ghi file vào thư mục uploads
        try (InputStream fileContent = filePart.getInputStream();
             FileOutputStream fos = new FileOutputStream(new File(uploadPath, uniqueFileName))) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fileContent.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            response.sendRedirect("profile.jsp?error=UploadFailed");
            return;
        }

        // Cập nhật ảnh đại diện của user
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user != null) {
            String avatarPath = "uploads/" + uniqueFileName;
            user.setAvatarUrl(avatarPath); // Cập nhật trong session

            // Cập nhật trong database (nếu có)
            UserDAO dao = new UserDAO();
            dao.updateAvatar(user.getUserId(), avatarPath); // Giả sử bạn có hàm này
        }

        response.sendRedirect("profile.jsp");
    }
}