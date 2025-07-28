package controller;

import dao.ExerciseDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "EditExerciseServlet", urlPatterns = {"/EditExerciseServlet"})
public class EditExerciseServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int exerciseId = Integer.parseInt(request.getParameter("exerciseId"));
            int exerciseLibraryId = Integer.parseInt(request.getParameter("exerciseLibraryId"));
            int sets = Integer.parseInt(request.getParameter("sets"));
            int reps = Integer.parseInt(request.getParameter("reps"));
            String restTimeStr = request.getParameter("restTime");
            String notes = request.getParameter("notes");

            // Validation
            if (sets <= 0 || reps <= 0) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Sets and reps must be greater than 0.");
                return;
            }

            Integer restTime = null;
            if (restTimeStr != null && !restTimeStr.trim().isEmpty()) {
                restTime = Integer.parseInt(restTimeStr);
                if (restTime < 0) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Rest time cannot be negative.");
                    return;
                }
            }

            ExerciseDAO exerciseDAO = new ExerciseDAO();
            boolean success = exerciseDAO.updateExercise(exerciseId, exerciseLibraryId, sets, reps, restTime, notes);
            
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
                    "Failed to update exercise.");
            }

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid input data.");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                "An error occurred while updating the exercise.");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
}
