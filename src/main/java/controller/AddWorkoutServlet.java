/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import dao.ProgramDayDAO;
import dao.WorkoutDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet(name="AddWorkoutServlet", urlPatterns={"/AddWorkoutServlet"})
public class AddWorkoutServlet extends HttpServlet {

     @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int dayId = Integer.parseInt(request.getParameter("dayId"));
            String title = request.getParameter("title");
            String rounds = request.getParameter("rounds");
            String notes = request.getParameter("notes");

            // Tạo workout mới
            WorkoutDAO workoutDAO = new WorkoutDAO();
            workoutDAO.createWorkout(dayId, title, rounds, notes);

            // Lấy programId từ dayId
            ProgramDayDAO dayDAO = new ProgramDayDAO();
            int programId = dayDAO.getProgramIdByDayId(dayId);

            response.sendRedirect("ProgramDetailServlet?programId=" + programId);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(500, "Error adding workout.");
        }
    }
}
