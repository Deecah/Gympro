package controller;

import Utils.CloudinaryUploader;
import dao.ExerciseLibraryDAO;
import model.ExerciseLibrary;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.InputStream;

@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 10, // 10 MB
        maxFileSize = 1024 * 1024 * 100, // 100 MB
        maxRequestSize = 1024 * 1024 * 150 // 150 MB
)
@WebServlet("/EditExerciseServlet")
public class EditExerciseServlet extends HttpServlet {

    private final ExerciseLibraryDAO exerciseDAO = new ExerciseLibraryDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try {
            System.out.println("doPost started");
            int exerciseId = Integer.parseInt(request.getParameter("exerciseID"));
            System.out.println("Parsed exerciseId: " + exerciseId);
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            String muscleGroup = request.getParameter("muscleGroup");
            String equipment = request.getParameter("equipment");
            String existingVideoURL = request.getParameter("existingVideoURL");

            // Validate required fields
            if (name == null || name.trim().isEmpty()
                    || description == null || description.trim().isEmpty()
                    || muscleGroup == null || muscleGroup.trim().isEmpty()) {

                ExerciseLibrary exercise = new ExerciseLibrary();
                exercise.setExerciseID(exerciseId);
                exercise.setName(name);
                exercise.setDescription(description);
                exercise.setMuscleGroup(muscleGroup);
                exercise.setEquipment(equipment);
                exercise.setVideoURL(existingVideoURL);

                request.setAttribute("exercise", exercise);
                request.setAttribute("error", "Please fill in all required information!");
                request.getRequestDispatcher("/trainer/edit-exercise.jsp").forward(request, response);
                return;
            }

            // Handle video upload (optional)
            Part videoPart = request.getPart("videoFile");
            boolean isNewVideoUploaded = videoPart != null && videoPart.getSize() > 0
                    && videoPart.getSubmittedFileName() != null && !videoPart.getSubmittedFileName().isEmpty()
                    && videoPart.getContentType() != null && videoPart.getContentType().startsWith("video/");

            String videoURL = null;

            if (isNewVideoUploaded) {
                try (InputStream videoStream = videoPart.getInputStream()) {
                    // Optional: organize by exerciseId folder or trainerId folder
                    videoURL = CloudinaryUploader.upload(videoStream, videoPart.getContentType(), "exercises/edit_" + exerciseId);
                } catch (Exception e) {
                    e.printStackTrace();

                    ExerciseLibrary exercise = new ExerciseLibrary();
                    exercise.setExerciseID(exerciseId);
                    exercise.setName(name);
                    exercise.setDescription(description);
                    exercise.setMuscleGroup(muscleGroup);
                    exercise.setEquipment(equipment);
                    exercise.setVideoURL(existingVideoURL);

                    request.setAttribute("exercise", exercise);
                    request.setAttribute("error", "Error uploading video.");
                    request.getRequestDispatcher("/trainer/edit-exercise.jsp").forward(request, response);
                    return;
                }
            } else if (existingVideoURL != null && !existingVideoURL.trim().isEmpty()) {
                videoURL = existingVideoURL;
            } else {
                // No video at all - reject
                ExerciseLibrary exercise = new ExerciseLibrary();
                exercise.setExerciseID(exerciseId);
                exercise.setName(name);
                exercise.setDescription(description);
                exercise.setMuscleGroup(muscleGroup);
                exercise.setEquipment(equipment);
                exercise.setVideoURL(null);

                request.setAttribute("exercise", exercise);
                request.setAttribute("error", "Please upload a video or keep the existing one.");
                request.getRequestDispatcher("/trainer/edit-exercise.jsp").forward(request, response);
                return;
            }

            // Update exercise
            ExerciseLibrary exercise = new ExerciseLibrary();
            exercise.setExerciseID(exerciseId);
            exercise.setName(name);
            exercise.setDescription(description);
            exercise.setMuscleGroup(muscleGroup);
            exercise.setEquipment(equipment);
            exercise.setVideoURL(videoURL);

            boolean success = exerciseDAO.updateExercise(exercise);

            if (success) {
                response.sendRedirect(request.getContextPath() + "/trainer/library.jsp?action=list");
            } else {
                request.setAttribute("exercise", exercise);
                request.setAttribute("error", "Failed to update exercise.");
                request.getRequestDispatcher("/trainer/edit-exercise.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();

            // Repopulate form with user input if error occurs
            ExerciseLibrary exercise = new ExerciseLibrary();
            try {
                exercise.setExerciseID(Integer.parseInt(request.getParameter("exerciseId")));
            } catch (Exception ignored) {
            }
            exercise.setName(request.getParameter("name"));
            exercise.setDescription(request.getParameter("description"));
            exercise.setMuscleGroup(request.getParameter("muscleGroup"));
            exercise.setEquipment(request.getParameter("equipment"));
            exercise.setVideoURL(request.getParameter("existingVideoURL"));

            request.setAttribute("exercise", exercise);
            request.setAttribute("error", "Unexpected error occurred. Please try again.");
            request.getRequestDispatcher("/trainer/edit-exercise.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        ExerciseLibrary exercise = exerciseDAO.getExerciseById(id);

        request.setAttribute("exercise", exercise);
        request.getRequestDispatcher("/trainer/edit-exercise.jsp").forward(request, response);
    }
}
