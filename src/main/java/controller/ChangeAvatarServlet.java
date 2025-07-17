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

        String appPath = request.getServletContext().getRealPath("");
        String uploadPath = appPath + File.separator + UPLOAD_DIR;

        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        String filePath = uploadPath + File.separator + fileName;
        filePart.write(filePath);

        String avatarUrl = UPLOAD_DIR + "/" + fileName;

        UserDAO userDAO = new UserDAO();
        user.setAvatarUrl(avatarUrl);
        userDAO.updateAvatar(user.getUserId(), avatarUrl);

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