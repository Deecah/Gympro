/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import dao.ExerciseDAO;
import dao.ProgramDayDAO;
import dao.WorkoutDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
@WebServlet(name="AddExerciseServlet", urlPatterns={"/AddExerciseServlet"})
public class AddExerciseServlet extends HttpServlet {

      @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int workoutId = Integer.parseInt(request.getParameter("workoutId"));
            int exerciseId = Integer.parseInt(request.getParameter("exerciseId"));
            int sets = Integer.parseInt(request.getParameter("sets"));
            int reps = Integer.parseInt(request.getParameter("reps"));
            int restTime = Integer.parseInt(request.getParameter("restTime"));
            String notes = request.getParameter("notes");

            // Thêm bài tập vào workout
            ExerciseDAO dao = new ExerciseDAO();
            dao.addExerciseToWorkout(workoutId, exerciseId, sets, reps, restTime, notes);

            // Lấy programId từ workoutId -> dayId -> programId
            WorkoutDAO workoutDAO = new WorkoutDAO();
            int dayId = workoutDAO.getDayIdByWorkoutId(workoutId);
            int programId = new ProgramDayDAO().getProgramIdByDayId(dayId);

            response.sendRedirect("ProgramDetailServlet?programId=" + programId);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(500, "Error adding exercise.");
        }
    }
    
}
