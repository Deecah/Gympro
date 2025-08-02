package controller;

import dao.ExerciseProgramDAO;
import model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/AddExerciseToProgramServlet")
public class AddExerciseToProgramServlet extends HttpServlet {
    private ExerciseProgramDAO exerciseProgramDAO = new ExerciseProgramDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
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
        if (userId == null || role == null || !role.equalsIgnoreCase("Trainer")) {
            response.sendRedirect("login.jsp");
            return;
        }

        int programId = Integer.parseInt(request.getParameter("programId"));
        String[] exerciseLibraryIds = request.getParameterValues("exerciseLibraryIds");

        if (exerciseLibraryIds == null || exerciseLibraryIds.length == 0) {
            response.sendRedirect("ProgramServlet?programId=" + programId + "&error=No exercises selected");
            return;
        }

        List<Integer> exerciseIds = Arrays.stream(exerciseLibraryIds)
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        boolean success = exerciseProgramDAO.addExercisesToProgram(programId, exerciseIds, Integer.parseInt(userId));
        if (success) {
            response.sendRedirect("ProgramServlet?programId=" + programId + "&success=Exercises added successfully");
        } else {
            response.sendRedirect("ProgramServlet?programId=" + programId + "&error=Failed to add exercises");
        }
    }
}