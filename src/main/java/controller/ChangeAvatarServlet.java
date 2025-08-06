package controller;

import dao.UserDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.File;
import java.nio.file.Paths;
import model.User;
import Utils.CloudinaryUploader;
import Utils.NotificationUtil;

@WebServlet(name = "ChangeAvatarServlet", urlPatterns = {"/ChangeAvatarServlet"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 5,
        maxRequestSize = 1024 * 1024 * 10
)
public class ChangeAvatarServlet extends HttpServlet {

    private static final String UPLOAD_DIR = "uploads";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        Part filePart = request.getPart("avatar");
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

        String avatarUrl = null;
        try {
            String folder = "avatars/user_" + user.getUserId();
            avatarUrl = CloudinaryUploader.upload(filePart.getInputStream(), filePart.getContentType(), folder);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi khi upload ảnh lên Cloudinary.");
            String role = user.getRole();
            String redirectUrl;
            switch (role.toLowerCase()) {
                case "trainer":
                    redirectUrl = "trainer/profile-trainer.jsp";
                    break;
                case "customer":
                    redirectUrl = "profile.jsp";
                    break;
                default:
                    redirectUrl = "home.jsp";
                    break;
            }
            response.sendRedirect(redirectUrl);
            return;
        }

        UserDAO userDAO = new UserDAO();
        user.setAvatarUrl(avatarUrl);
        boolean success = userDAO.updateAvatar(user.getUserId(), avatarUrl);
        
        if (success) {
            NotificationUtil.sendSuccessNotification(user.getUserId(), 
                "Avatar Updated Successfully", 
                "Your profile picture has been updated successfully!");
        }

        session.setAttribute("user", user);
        String role = user.getRole(); 
        String redirectUrl;
        switch (role.toLowerCase()) {
            case "trainer":
                redirectUrl = "trainer/profile-trainer.jsp";
                break;
            case "customer":
                redirectUrl = "profile.jsp";
                break;
            default:
                redirectUrl = "home.jsp"; 
                break;
        }
        response.sendRedirect(redirectUrl);
    }
}