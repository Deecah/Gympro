package controller;

import dao.ExerciseProgramDAO;
import model.ExerciseLibrary;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import com.google.gson.Gson;

@WebServlet("/ViewExercisesServlet")
public class ViewExercisesServlet extends HttpServlet {
    private ExerciseProgramDAO exerciseProgramDAO = new ExerciseProgramDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int programId = Integer.parseInt(request.getParameter("programId"));
        List<ExerciseLibrary> exercises = exerciseProgramDAO.getExercisesByProgram(programId);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(new Gson().toJson(exercises));
        out.flush();
    }
}