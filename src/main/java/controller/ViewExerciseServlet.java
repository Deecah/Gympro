package controller;

import dao.ExerciseLibraryDAO;
import model.ExerciseLibrary;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ViewExerciseServlet", urlPatterns = {"/ViewExerciseServlet"})
public class ViewExerciseServlet extends HttpServlet {

    private final ExerciseLibraryDAO exerciseDAO = new ExerciseLibraryDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String userId = null;
        String role = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equalsIgnoreCase("userId") && cookie.getValue() != null) {
                userId = cookie.getValue();
            } else if (cookie.getName().equalsIgnoreCase("role") && cookie.getValue() != null) {
                role = cookie.getValue();
            }
        }
        // Check if user is authenticated and is a trainer
        if (userId == null || role == null || !role.equalsIgnoreCase("Trainer")) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Get list of all exercises
        List<ExerciseLibrary> exercises = exerciseDAO.getAllExercises(Integer.parseInt(userId));

        // Set the list as a request attribute for JSP
        request.setAttribute("exercises", exercises);

        // Forward to JSP page that displays the exercises
        request.getRequestDispatcher("trainer/library.jsp").forward(request, response);
    }
}
