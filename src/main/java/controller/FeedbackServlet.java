package controller;

import dao.FeedbackDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/feedback")
public class FeedbackServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int userId = Integer.parseInt(request.getParameter("userId"));
        String type = request.getParameter("type"); // 'package' or 'trainer'
        int referenceId = Integer.parseInt(request.getParameter("referenceId"));
        int point = Integer.parseInt(request.getParameter("point"));
        String content = request.getParameter("content");

        FeedbackDAO dao = new FeedbackDAO();
        boolean success = dao.insertFeedback(userId, type, referenceId, point, content);

        if (success) {
            HttpSession session = request.getSession();
            session.setAttribute("feedbackSuccess", true);
            response.sendRedirect("packagesPurchased");
        } else {
            response.sendRedirect("packagesPurchased.jsp?feedback=fail");
        }
    }
}
