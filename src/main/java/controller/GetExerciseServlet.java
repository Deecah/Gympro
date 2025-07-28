package controller;

import dao.ExerciseDAO;
import model.Exercise;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "GetExerciseServlet", urlPatterns = {"/GetExerciseServlet"})
public class GetExerciseServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        PrintWriter out = response.getWriter();
        
        try {
            int exerciseId = Integer.parseInt(request.getParameter("exerciseId"));
            
            ExerciseDAO exerciseDAO = new ExerciseDAO();
            Exercise exercise = exerciseDAO.getExerciseById(exerciseId);
            
            if (exercise != null) {
                // Create JSON manually to avoid any serialization issues
                String json = "{" +
                    "\"exerciseId\":" + exercise.getExerciseId() + "," +
                    "\"workoutID\":" + exercise.getWorkoutID() + "," +
                    "\"sets\":" + exercise.getSets() + "," +
                    "\"reps\":" + exercise.getReps() + "," +
                    "\"restTimeSeconds\":" + exercise.getRestTimeSeconds() + "," +
                    "\"notes\":\"" + (exercise.getNotes() != null ? exercise.getNotes().replace("\"", "\\\"") : "") + "\"," +
                    "\"exerciseName\":\"" + (exercise.getExerciseName() != null ? exercise.getExerciseName().replace("\"", "\\\"") : "") + "\"," +
                    "\"videoURL\":\"" + (exercise.getVideoURL() != null ? exercise.getVideoURL().replace("\"", "\\\"") : "") + "\"," +
                    "\"description\":\"" + (exercise.getDescription() != null ? exercise.getDescription().replace("\"", "\\\"") : "") + "\"" +
                    "}";
                out.println(json);
            } else {
                out.println("{\"error\": \"Exercise not found\"}");
            }
            
        } catch (NumberFormatException e) {
            out.println("{\"error\": \"Invalid exercise ID\"}");
        } catch (Exception e) {
            out.println("{\"error\": \"An error occurred while fetching the exercise\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
} 