package controller;

import dao.ProgramDayDAO;
import dao.WorkoutDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.time.LocalTime;

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
            workoutDAO.createWorkout(dayId, title, notes, startTime, endTime);

            ProgramDayDAO dayDAO = new ProgramDayDAO();
            int programId = dayDAO.getProgramIdByDayId(dayId);

            response.sendRedirect("ProgramDetailServlet?programId=" + programId);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error adding workout.");
        }
    }
}
