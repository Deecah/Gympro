package controller;

import dao.ProgressDAO;
import dao.UserDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Customer;
import model.Progress;

import java.io.IOException;
import java.util.List;
import model.User;

@WebServlet("/progress")
public class ProgressServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");

        if (userId != null && role != null) {
            UserDAO userDAO = new UserDAO();
            User user = userDAO.getUserById(userId);

            if (user != null) {
                List<Progress> progressList = ProgressDAO.getProgressByUserID(userId);
                request.setAttribute("progressList", progressList);
                request.setAttribute("user", user); // ✅ Gửi user sang view
            }
        }

        request.getRequestDispatcher("viewProgress.jsp").forward(request, response);
    }

}
