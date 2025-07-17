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
        
        // Optional: Check if user is logged in; skip if public
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null) {
            // Redirect to login or show public view, depending on your design
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // Get list of all exercises
        List<ExerciseLibrary> exercises = exerciseDAO.getAllExercises();

        // Set the list as a request attribute for JSP
        request.setAttribute("exercises", exercises);

        // Forward to JSP page that displays the exercises
        request.getRequestDispatcher("trainer/library.jsp").forward(request, response);
    }
}
