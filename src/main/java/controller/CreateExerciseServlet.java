package controller;

import dao.ExerciseLibraryDAO;
import dao.ExerciseDAO;
import model.ExerciseLibrary;
import model.Exercise;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@WebServlet("/CreateExerciseServlet")
@MultipartConfig
public class CreateExerciseServlet extends HttpServlet {
    private ExerciseLibraryDAO exerciseLibraryDAO = new ExerciseLibraryDAO();
    private ExerciseDAO exerciseDAO = new ExerciseDAO();

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
        // Check if user is authenticated and is a trainer
        if (userId == null || role == null || !role.equalsIgnoreCase("Trainer")) {
            response.sendRedirect("login.jsp");
            return;
        }
        String exerciseLibraryId = request.getParameter("exerciseLibraryId");
        String exerciseName = request.getParameter("exerciseName");
        String description = request.getParameter("description");
        String muscleGroup = request.getParameter("muscleGroup");
        String equipment = request.getParameter("equipment");
        int sets = Integer.parseInt(request.getParameter("sets"));
        int reps = Integer.parseInt(request.getParameter("reps"));
        int rest = Integer.parseInt(request.getParameter("restTimeSeconds"));
        Part videoPart = request.getPart("videoFile");

        ExerciseLibrary exercise = new ExerciseLibrary();
        exercise.setName(exerciseName);
        exercise.setDescription(description);
        exercise.setMuscleGroup(muscleGroup);
        exercise.setEquipment(equipment);
        exercise.setSets(sets);
        exercise.setReps(reps);
        exercise.setRestTimeSeconds(rest);
        exercise.setTrainerID(Integer.parseInt(userId));

        String videoUrl = null;
        if (videoPart != null && videoPart.getSize() > 0) {
            String fileName = Paths.get(videoPart.getSubmittedFileName()).getFileName().toString();
            String uploadPath = getServletContext().getRealPath("") + "uploads/videos/";
            Files.createDirectories(Paths.get(uploadPath));
            String filePath = uploadPath + fileName;
            videoPart.write(filePath);
            videoUrl = request.getContextPath() + "/uploads/videos/" + fileName;
            exercise.setVideoURL(videoUrl);
        }

        int exerciseId;
        if (exerciseLibraryId != null && !exerciseLibraryId.isEmpty()) {
            exerciseId = Integer.parseInt(exerciseLibraryId);
        } else {
            exerciseId = exerciseLibraryDAO.insertExercise(exercise);
        }

        if (exerciseId != -1) {
            response.sendRedirect("trainer/library.jsp?action=list&success=Exercise created successfully");
        } else {
            response.sendRedirect("trainer/create-exercise.jsp?error=Failed to create exercise");
        }
    }
}