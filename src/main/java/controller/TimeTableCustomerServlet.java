package controller;

import connectDB.ConnectDatabase;
import dao.TimetableDAO;
import dao.ProgressDAO;
import dao.WorkoutDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.CustomerWorkoutSchedule;
import model.ExerciseProgram;
import model.Workout;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.Gson;

@WebServlet({"/TimetableCustomerServlet"})
@MultipartConfig
public class TimeTableCustomerServlet extends HttpServlet {

    private TimetableDAO timetableDAO = new TimetableDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        if ("/workoutDetail".equals(path)) {
            int workoutId = Integer.parseInt(request.getParameter("workoutId"));
            Object workoutDetail = timetableDAO.getWorkoutDetail(workoutId);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(new Gson().toJson(workoutDetail));
            out.flush();
            return;
        }

        HttpSession session = request.getSession();
        String userId = null;
        String role = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equalsIgnoreCase("userId") && cookie.getValue() != null) {
                    userId = cookie.getValue();
                } else if (cookie.getName().equalsIgnoreCase("role") && cookie.getValue() != null) {
                    role = cookie.getValue();
                }
            }
        }
        if (userId == null || role == null || !role.equalsIgnoreCase("Customer")) {
            response.sendRedirect("login.jsp");
            return;
        }

        int customerProgramId = Integer.parseInt(request.getParameter("customerProgramId"));
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String weekStartStr = request.getParameter("weekStartDate");
        int scheduleId = Integer.parseInt(request.getParameter("scheduleId"));
        LocalDate weekStartDate;
        try {
            weekStartDate = weekStartStr != null && !weekStartStr.isEmpty() ?
                    LocalDate.parse(weekStartStr, DateTimeFormatter.ISO_LOCAL_DATE).with(DayOfWeek.MONDAY) :
                    LocalDate.now().with(java.time.DayOfWeek.MONDAY);
        } catch (DateTimeParseException e) {
            weekStartDate = LocalDate.now().with(java.time.DayOfWeek.MONDAY);
        }
        weekStartStr = weekStartDate.toString();

        // Lấy programId từ CustomerProgram
        int programId = getProgramIdFromCustomerProgram(customerProgramId);

        List<CustomerWorkoutSchedule> schedules = timetableDAO.getSchedulesForWeek(customerProgramId, weekStartStr);
        List<ExerciseProgram> exercisePrograms = timetableDAO.getExerciseProgramsByProgramId(programId);
        LocalDate startLocalDate = LocalDate.parse(startDate);
        LocalDate endLocalDate = LocalDate.parse(endDate);
        // Always use Monday for week start
        LocalDate firstMonday = startLocalDate.with(DayOfWeek.MONDAY);
        LocalDate lastMonday = endLocalDate.with(DayOfWeek.MONDAY);
        List<String> weekOptions = new ArrayList<>();
        for (LocalDate d = firstMonday; !d.isAfter(lastMonday); d = d.plusWeeks(1)) {
            weekOptions.add(d.toString());
        }

        // Add weekDates for JSP (each day in the week)
        List<LocalDate> weekDates = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            weekDates.add(weekStartDate.plusDays(i));
        }
        request.setAttribute("customerProgramId", customerProgramId);
        request.setAttribute("weekDates", weekDates);
        request.setAttribute("scheduleId", scheduleId);
        request.setAttribute("schedules", schedules);
        request.setAttribute("exercisePrograms", exercisePrograms);
        request.setAttribute("weekOptions", weekOptions);
        request.setAttribute("programId", programId);
        request.setAttribute("startDate", LocalDate.parse(startDate));
        request.setAttribute("endDate", LocalDate.parse(endDate));
        request.setAttribute("weekStartDate", weekStartDate);
        request.getRequestDispatcher("timetable.jsp").forward(request, response);
    }

    private int getProgramIdFromCustomerProgram(int customerProgramId) {
        String sql = "SELECT ProgramID FROM CustomerProgram WHERE Id = ?";
        try (Connection conn = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerProgramId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("ProgramID");
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

}