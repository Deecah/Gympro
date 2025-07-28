package controller;

import dao.FeedbackDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import Utils.NotificationUtil;

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
            // Gửi notification thành công
            NotificationUtil.sendSuccessNotification(userId, 
                "Feedback Submitted Successfully", 
                "Thank you for your feedback! Your review has been submitted successfully.");
            response.sendRedirect("PackagesPurchasedServlet?feedback=success");
        } else {
            // Gửi notification lỗi
            NotificationUtil.sendErrorNotification(userId, 
                "Feedback Submission Failed", 
                "Failed to submit your feedback. Please try again.");
            response.sendRedirect("packagesPurchased.jsp?feedback=fail");
        }
    }
}
