package controller;

import dao.WorkoutDAO;
import model.Workout;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;

@WebServlet("/WorkoutServlet")
public class WorkoutServlet extends HttpServlet {
    private WorkoutDAO dao = new WorkoutDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("add".equals(action)) {
            Date date = Date.valueOf(request.getParameter("date"));
            LocalTime startTime = LocalTime.parse(request.getParameter("startTime"));
            LocalTime endTime = LocalTime.parse(request.getParameter("endTime"));
            int scheduleId = Integer.parseInt(request.getParameter("scheduleId"));
            int customerProgramId = Integer.parseInt(request.getParameter("customerProgramId"));
            String trainerId = null;
            String role = null;
            Cookie[] cookies = request.getCookies();
            for (Cookie cookie : cookies) {
                if (cookie.getName().equalsIgnoreCase("userId") && cookie.getValue() != null) {
                    trainerId = cookie.getValue();
                } else if (cookie.getName().equalsIgnoreCase("role") && cookie.getValue() != null) {
                    role = cookie.getValue();
                }
            }
            if (trainerId == null || role == null || !role.equalsIgnoreCase("Trainer")) {
                response.sendRedirect("login.jsp");
                return;
            }

            Workout w = new Workout();
            w.setStatus("pending");
            w.setStartTime(Time.valueOf(startTime));
            w.setEndTime(Time.valueOf(endTime));
            w.setDate(date);
            w.setScheduleId(scheduleId);
            w.setTrainerId(Integer.parseInt(trainerId));
            w.setCustomerProgramId(customerProgramId);
            int success = dao.addWorkout(w);
            if (success!=-1) {
                response.sendRedirect("program-detail.jsp?date=" + date);
            } else {
                request.setAttribute("error", "Failed to add workout.");
                request.getRequestDispatcher("/error.jsp").forward(request, response);
            }
        }
    }
}