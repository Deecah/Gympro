package controller;

import Utils.CloudinaryUploader;
import dao.ExerciseLibraryDAO;
import model.ExerciseLibrary;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.InputStream;
import Utils.NotificationUtil;

@WebServlet(name = "CreateExerciseServlet", urlPatterns = {"/CreateExerciseServlet"})
@MultipartConfig
public class CreateExerciseServlet extends HttpServlet {

    private final ExerciseLibraryDAO exerciseDAO = new ExerciseLibraryDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null || !"Trainer".equalsIgnoreCase(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        request.getRequestDispatcher("trainer/create-exercise.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null || !"Trainer".equalsIgnoreCase(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        request.setCharacterEncoding("UTF-8");

        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String muscleGroup = request.getParameter("muscleGroup");
        String equipment = request.getParameter("equipment");

        // Validate required fields
        if (name == null || name.trim().isEmpty()
                || description == null || description.trim().isEmpty()
                || muscleGroup == null || muscleGroup.trim().isEmpty()) {

            request.setAttribute("error", "Please fill in the required information!");
            request.getRequestDispatcher("trainer/create-exercise.jsp").forward(request, response);
            return;
        }

        // Handle video upload (optional)
        String videoURL = null;
        Part videoPart = request.getPart("videoFile");

        boolean isValidVideo = videoPart != null
                && videoPart.getSize() > 0
                && videoPart.getSubmittedFileName() != null
                && !videoPart.getSubmittedFileName().isEmpty()
                && videoPart.getContentType() != null
                && videoPart.getContentType().startsWith("video/");

        if (isValidVideo) {
            try (InputStream videoStream = videoPart.getInputStream()) {
                String folder = "exercises/trainer_" + user.getUserId();
                videoURL = CloudinaryUploader.upload(videoStream, videoPart.getContentType(), folder);
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("error", "Error uploading video!");
                request.getRequestDispatcher("trainer/create-exercise.jsp").forward(request, response);
                return;
            }
        }

        // Create Exercise object
        ExerciseLibrary exercise = new ExerciseLibrary();
        exercise.setName(name);
        exercise.setDescription(description);
        exercise.setMuscleGroup(muscleGroup);
        exercise.setEquipment(equipment);
        exercise.setVideoURL(videoURL);

        boolean success = exerciseDAO.insertExercise(exercise);
        if (success) {
            response.sendRedirect(request.getContextPath() + "/trainer/library.jsp?action=list");
        } else {
            request.setAttribute("error", "Can't save exercise!");
            request.getRequestDispatcher("trainer/create-exercise.jsp").forward(request, response);
        }
    }
}
