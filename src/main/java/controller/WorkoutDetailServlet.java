package controller;

import dao.WorkoutDAO;
import dao.ExerciseDAO;
import model.Workout;
import model.Exercise;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.List;

@WebServlet("/workoutDetail")
public class WorkoutDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String workoutIdStr = req.getParameter("workoutId");
        if (workoutIdStr == null || workoutIdStr.trim().isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Workout ID is required");
            return;
        }

        try {
            int workoutId = Integer.parseInt(workoutIdStr);
            
            WorkoutDAO workoutDAO = new WorkoutDAO();
            ExerciseDAO exerciseDAO = new ExerciseDAO();
            
            // Lấy thông tin workout
            Workout workout = workoutDAO.getWorkoutById(workoutId);
            if (workout == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Workout not found");
                return;
            }
            
            // Lấy danh sách exercises của workout
            List<Exercise> exercises = exerciseDAO.getExercisesByWorkout(workoutId);
            
            // Tạo JSON response
            JsonObject response = new JsonObject();
            JsonObject workoutJson = new JsonObject();
            
            workoutJson.addProperty("workoutId", workout.getWorkoutID());
            workoutJson.addProperty("title", workout.getTitle());
            workoutJson.addProperty("notes", workout.getNotes() != null ? workout.getNotes() : "");
            workoutJson.addProperty("startTime", workout.getStartStr());
            workoutJson.addProperty("endTime", workout.getEndStr());
            workoutJson.addProperty("programName", workout.getProgramName() != null ? workout.getProgramName() : "");
            
            response.add("workout", workoutJson);
            
            // Thêm exercises
            Gson gson = new Gson();
            response.add("exercises", gson.toJsonTree(exercises));
            
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write(gson.toJson(response));
            
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid workout ID");
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server error");
        }
    }
} 