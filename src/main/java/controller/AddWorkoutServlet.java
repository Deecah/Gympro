package controller;

import dao.ProgramDayDAO;
import dao.WorkoutDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.time.LocalTime;
import Utils.NotificationUtil;
import model.User;
import java.util.List;

@WebServlet(name = "AddWorkoutServlet", urlPatterns = {"/AddWorkoutServlet"})
public class AddWorkoutServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int dayId = Integer.parseInt(request.getParameter("dayId"));
            String title = request.getParameter("title");
            String notes = request.getParameter("notes");

            LocalTime startTime = LocalTime.parse(request.getParameter("startTime"));
            LocalTime endTime = LocalTime.parse(request.getParameter("endTime"));

            if (!startTime.isBefore(endTime)) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "End time must be after start time.");
                return;
            }

            WorkoutDAO workoutDAO = new WorkoutDAO();
            // Kiểm tra trùng thời gian
            List<model.Workout> existingWorkouts = workoutDAO.getWorkoutsByDayId(dayId);
            boolean overlap = false;
            for (model.Workout w : existingWorkouts) {
                if (w.getStartTime() != null && w.getEndTime() != null &&
                    !(endTime.isBefore(w.getStartTime()) || startTime.isAfter(w.getEndTime()))) {
                    overlap = true;
                    break;
                }
            }
            if (overlap) {
                ProgramDayDAO dayDAO = new ProgramDayDAO();
                int programId = dayDAO.getProgramIdByDayId(dayId);
                response.sendRedirect("ProgramDetailServlet?programId=" + programId + "&error=overlap");
                return;
            }

            int result = workoutDAO.createWorkout(dayId, title, notes, startTime, endTime);
            boolean success = result > 0;
            
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
            
            if (success && user != null) {
                NotificationUtil.sendSuccessNotification(user.getUserId(), 
                    "Workout Added Successfully", 
                    "Your new workout '" + title + "' has been added to the program successfully!");
            } else if (user != null) {
                NotificationUtil.sendErrorNotification(user.getUserId(), 
                    "Workout Addition Failed", 
                    "Failed to add workout '" + title + "'. Please try again.");
            }

            ProgramDayDAO dayDAO = new ProgramDayDAO();
            int programId = dayDAO.getProgramIdByDayId(dayId);

            response.sendRedirect("ProgramDetailServlet?programId=" + programId);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error adding workout.");
        }
    }
}
