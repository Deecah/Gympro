package controller;

import dao.ExerciseLibraryDAO;
import dao.ExerciseProgramDAO;
import model.ExerciseLibrary;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.Gson;

@WebServlet("/GetAvailableExercisesServlet")
public class GetAvailableExercisesServlet extends HttpServlet {
    private ExerciseProgramDAO exerciseProgramDAO = new ExerciseProgramDAO();
    private ExerciseLibraryDAO exerciseLibraryDAO = new ExerciseLibraryDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
        // Check if user is authenticated and is a trainer
        if (userId == null || role == null || !role.equalsIgnoreCase("Trainer")) {
            response.sendRedirect("login.jsp");
            return;
        }
        int programId = Integer.parseInt(request.getParameter("programId"));
        List<ExerciseLibrary> exercises = exerciseLibraryDAO.getAllExercises(Integer.parseInt(userId));
        List<ExerciseLibrary> programExercises = exerciseProgramDAO.getExercisesByProgram(programId);

        List<ExerciseLibrary> newExercises = exercises.stream()
            .filter(ex -> programExercises.stream()
                .noneMatch(pe -> pe.getExerciseID() == ex.getExerciseID()))
            .collect(Collectors.toList());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(new Gson().toJson(newExercises));
        out.flush();
    }
}