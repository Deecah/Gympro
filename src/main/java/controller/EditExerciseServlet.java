package controller;

import dao.ExerciseLibraryDAO;
import model.ExerciseLibrary;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/EditExerciseServlet")
public class EditExerciseServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int exerciseId = Integer.parseInt(request.getParameter("exerciseId"));
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            String videoURL = request.getParameter("videoURL");
            String muscleGroup = request.getParameter("muscleGroup");
            String equipment = request.getParameter("equipment");

            ExerciseLibrary exercise = new ExerciseLibrary();
            exercise.setExerciseID(exerciseId);
            exercise.setName(name);
            exercise.setDescription(description);
            exercise.setVideoURL(videoURL);
            exercise.setMuscleGroup(muscleGroup);
            exercise.setEquipment(equipment);

            ExerciseLibraryDAO dao = new ExerciseLibraryDAO();
            dao.updateExercise(exercise);

            response.sendRedirect("trainer/library.jsp"); // redirect after successful edit

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to update exercise.");
            request.getRequestDispatcher("/trainer/edit-exercise.jsp").forward(request, response);
        }
        // Get video file part
        Part videoPart = request.getPart("videoFile");

// Check if a new video is uploaded
        boolean isNewVideoUploaded = videoPart != null && videoPart.getSize() > 0;

// Also get the existing videoURL from hidden input or DB
        String existingVideoURL = request.getParameter("existingVideoURL"); // Or get from DB

// Validate video presence:
        if (!isNewVideoUploaded && (existingVideoURL == null || existingVideoURL.trim().isEmpty())) {
            // No video present after edit - reject
            request.setAttribute("error", "Please upload a video or keep the existing one.");
            // Re-populate exercise and forward back to edit page
            // ...
            return;
        }

// Proceed with update logic:
// - if new video uploaded: upload and update videoURL
// - else: keep existing videoURL

    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        ExerciseLibraryDAO dao = new ExerciseLibraryDAO();
        ExerciseLibrary exercise = dao.getExerciseById(id);

        request.setAttribute("exercise", exercise); 
        request.getRequestDispatcher("/trainer/edit-exercise.jsp").forward(request, response);
    }
}
