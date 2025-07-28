package controller;

import dao.WorkoutDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalTime;

@WebServlet(name = "EditWorkoutServlet", urlPatterns = {"/EditWorkoutServlet"})
public class EditWorkoutServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int workoutId = Integer.parseInt(request.getParameter("workoutId"));
            String title = request.getParameter("title");
            String notes = request.getParameter("notes");
            String startTimeStr = request.getParameter("startTime");
            String endTimeStr = request.getParameter("endTime");

            // Validation
            if (title == null || title.trim().isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Title is required.");
                return;
            }

            LocalTime startTime = LocalTime.parse(startTimeStr);
            LocalTime endTime = LocalTime.parse(endTimeStr);

            // Check time range (6:00 - 22:00)
            if (startTime.getHour() < 6 || startTime.getHour() > 22 || 
                endTime.getHour() < 6 || endTime.getHour() > 22) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, 
                    "Start time and end time must be between 06:00 and 22:00.");
                return;
            }

            // Check if end time is after start time
            if (!endTime.isAfter(startTime)) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, 
                    "End time must be after start time.");
                return;
            }

            WorkoutDAO workoutDAO = new WorkoutDAO();
            boolean success = workoutDAO.updateWorkout(workoutId, title, notes, startTime, endTime);
            
            if (success) {
                // Redirect back to program detail page
                String referer = request.getHeader("Referer");
                if (referer != null && !referer.isEmpty()) {
                    response.sendRedirect(referer);
                } else {
                    response.sendRedirect(request.getContextPath() + "/ProgramServlet");
                }
            } else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                    "Failed to update workout.");
            }

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid workout ID.");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                "An error occurred while updating the workout.");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
} 