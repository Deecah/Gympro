package controller;

import dao.ExerciseLibraryDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/DeleteExerciseLibraryServlet")
public class DeleteExerciseLibraryServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");

        if (idParam == null || idParam.trim().isEmpty()) {
            System.err.println("Missing or empty exercise ID.");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid exercise ID.");
            return;
        }

        try {
            int id = Integer.parseInt(idParam);
            ExerciseLibraryDAO dao = new ExerciseLibraryDAO();
            boolean deleted = dao.deleteExercise(id);

            if (deleted) {
                System.out.println("Deleted exercise with ID: " + id);
            } else {
                System.out.println("No exercise found to delete with ID: " + id);
            }

        } catch (NumberFormatException e) {
            System.err.println("Invalid exercise ID format: " + idParam);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid exercise ID.");
            return;
        }

        // Redirect regardless of result to keep UI consistent
        response.sendRedirect("/SWP391/trainer/library.jsp");
    }
}

