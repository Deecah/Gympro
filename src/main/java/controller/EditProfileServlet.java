package controller;


import dao.UserDAO;
import model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

public class EditProfileServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String gender = request.getParameter("gender");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String role = request.getParameter("role");
        String status = request.getParameter("status");

        User user = new User();
        user.setUserId(id);
        user.setUserName(name);
        user.setGender(gender);
        user.setEmail(email);
        user.setPhone(phone);
        user.setAddress(address);
        user.setRole(role);
        user.setStatus(status);

        UserDAO userDAO = new UserDAO();
        boolean success = userDAO.updateUser(user);

        if (success) {
            // Cập nhật lại session
            User updatedUser = userDAO.getUserById(id);
            HttpSession session = request.getSession();
            session.setAttribute("user", updatedUser);
        }

        // Chuyển hướng lại trang profile
        response.sendRedirect("profile.jsp");
    }
}
