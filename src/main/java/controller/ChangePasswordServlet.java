package controller;

import dao.UserDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.User;

public class ChangePasswordServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("changePassword.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String password1 = request.getParameter("password1");
        String password2 = request.getParameter("password2");

        if (password1 == null || password2 == null || !password1.equals(password2)) {
            request.setAttribute("error", "Passwords do not match!");
            request.getRequestDispatcher("changePassword.jsp").forward(request, response);
            return;
        }

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp"); // or wherever your login page is
            return;
        }

        User user = (User) session.getAttribute("user");

        try {
            UserDAO userDAO = new UserDAO();
            userDAO.updatePassword(user.getUserId(), password1.getBytes()); // assumes DB stores byte[]
            request.setAttribute("success", "Password changed successfully!");
        } catch (SQLException ex) {
            Logger.getLogger(ChangePasswordServlet.class.getName()).log(Level.SEVERE, null, ex);
            request.setAttribute("error", "An error occurred while changing the password.");
        }

        request.getRequestDispatcher("changePassword.jsp").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Handles password change requests";
    }
}
