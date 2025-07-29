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
        User user = (User) session.getAttribute("user"); // ✅ lấy đúng object

        if (user != null) {
            int userId = user.getUserId();
            String role = user.getRole();
            System.out.println(">>> userId from session: " + userId);
            System.out.println(">>> role from session: " + role);
            List<Progress> progressList = ProgressDAO.getProgressByUserID(userId);

            request.setAttribute("progressList", progressList);
            request.setAttribute("user", user); // Gửi sang JSP nếu cần hiển thị tên
        }

        request.getRequestDispatcher("viewProgress.jsp").forward(request, response);
    }
    
}
