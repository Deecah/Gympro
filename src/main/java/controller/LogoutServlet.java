package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import Utils.NotificationUtil;
import model.User;

@WebServlet(name = "LogoutServlet", urlPatterns = {"/logout"})
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false); 
        if (session != null) {
            User user = (User) session.getAttribute("user");
            if (user != null) {
                // Gửi notification logout
                NotificationUtil.sendInfoNotification(user.getUserId(), 
                    "Logged Out Successfully", 
                    "You have been logged out successfully. Thank you for using GymPro!");
            }
            session.invalidate();
        }

        response.sendRedirect("login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
