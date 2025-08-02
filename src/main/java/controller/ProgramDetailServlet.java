package controller;

import dao.*;
import model.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.*;

@WebServlet("/ProgramDetailServlet")
public class ProgramDetailServlet extends HttpServlet {
    private ProgramDAO programDAO = new ProgramDAO();
    private ProgramWeekDAO programWeekDAO = new ProgramWeekDAO();
    private ProgramDayDAO programDayDAO = new ProgramDayDAO();
    private ExerciseProgramDAO exerciseProgramDAO = new ExerciseProgramDAO();
    private WorkoutDAO workoutDAO = new WorkoutDAO();
    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null || !"trainer".equalsIgnoreCase(user.getRole())) {
            response.sendRedirect("login.jsp");
            return;
        }

        int programId = Integer.parseInt(request.getParameter("programId"));
        Program program = programDAO.getProgramById(programId);
        List<ProgramWeek> weeks = programWeekDAO.getWeeksByProgramId(programId);
        Map<Integer, List<ProgramDay>> daysMap = programDayDAO.get(programId);
        Map<Integer, List<Workout>> dayWorkouts = workoutDAO.getWorkoutsByProgram(programId);
        Map<Integer, List<ExerciseLibrary>> workoutExercises = workoutDAO.getExercisesByWorkouts(dayWorkouts);
        List<ExerciseLibrary> exerciseList = exerciseProgramDAO.getExercisesByProgram(programId);
        ArrayList<User> customers = userDAO.getCustomersByTrainer(user.getUserId());

        request.setAttribute("program", program);
        request.setAttribute("weeks", weeks);
        request.setAttribute("daysMap", daysMap);
        request.setAttribute("dayWorkouts", dayWorkouts);
        request.setAttribute("workoutExercises", workoutExercises);
        request.setAttribute("exerciseList", exerciseList);
        request.setAttribute("customers", customers);
        request.getRequestDispatcher("/trainer/program-detail.jsp").forward(request, response);
    }
}