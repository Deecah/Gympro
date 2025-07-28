package controller;

import com.google.gson.Gson;
import dao.WorkoutDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Workout;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "GetWorkoutServlet", urlPatterns = {"/GetWorkoutServlet"})
public class GetWorkoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        try {
            String workoutIdParam = request.getParameter("workoutId");
            if (workoutIdParam == null || workoutIdParam.trim().isEmpty()) {
                out.println("{\"error\": \"Workout ID is required\"}");
                return;
            }
            int workoutId = Integer.parseInt(workoutIdParam);
            System.out.println("Fetching workout with ID: " + workoutId);
            WorkoutDAO workoutDAO = new WorkoutDAO();
            Workout workout = workoutDAO.getWorkoutById(workoutId);
            if (workout != null) {
                System.out.println("Workout found: " + workout.getTitle());
                // Create JSON manually to avoid LocalTime serialization issues
                String json = "{" +
                    "\"workoutID\":" + workout.getWorkoutID() + "," +
                    "\"title\":\"" + (workout.getTitle() != null ? workout.getTitle().replace("\"", "\\\"") : "") + "\"," +
                    "\"notes\":\"" + (workout.getNotes() != null ? workout.getNotes().replace("\"", "\\\"") : "") + "\"," +
                    "\"startStr\":\"" + (workout.getStartStr() != null ? workout.getStartStr() : "") + "\"," +
                    "\"endStr\":\"" + (workout.getEndStr() != null ? workout.getEndStr() : "") + "\"" +
                    "}";
                System.out.println("JSON response: " + json);
                out.println(json);
            } else {
                System.out.println("Workout not found for ID: " + workoutId);
                out.println("{\"error\": \"Workout not found\"}");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid workout ID: " + e.getMessage());
            out.println("{\"error\": \"Invalid workout ID\"}");
        } catch (Exception e) {
            System.out.println("Error fetching workout: " + e.getMessage());
            e.printStackTrace();
            out.println("{\"error\": \"An error occurred while fetching the workout\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
} 