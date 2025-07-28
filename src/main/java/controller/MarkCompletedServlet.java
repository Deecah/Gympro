package controller;

import dao.ProgressDAO;
import Utils.NotificationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/markCompleted")
public class MarkCompletedServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        String workoutIdStr = req.getParameter("workoutId");
        if (workoutIdStr == null || workoutIdStr.trim().isEmpty()) {
            resp.sendRedirect("timetable");
            return;
        }

        try {
            int workoutId = Integer.parseInt(workoutIdStr);
            int customerId = ((model.User) session.getAttribute("user")).getUserId();
            String notes = req.getParameter("notes");
            
            ProgressDAO progressDAO = new ProgressDAO();
            boolean success = progressDAO.markWorkoutCompleted(customerId, workoutId, notes);
            
            if (success) {
                // Gửi notification thành công
                NotificationUtil.sendSuccessNotification(customerId, 
                    "Workout Completed", 
                    "You have successfully completed your workout!");
                resp.sendRedirect("timetable?msg=completed");
            } else {
                // Gửi notification lỗi
                NotificationUtil.sendErrorNotification(customerId, 
                    "Workout Completion Failed", 
                    "There was an error marking your workout as completed. Please try again.");
                resp.sendRedirect("timetable?msg=error");
            }
            
        } catch (NumberFormatException e) {
            resp.sendRedirect("timetable?msg=error");
        } catch (Exception e) {
            resp.sendRedirect("timetable?msg=error");
        }
    }
} 