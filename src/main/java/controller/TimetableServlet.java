package controller;

import dao.WorkoutDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Workout;

import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

@WebServlet("/timetable")
public class TimetableServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int customerId = ((model.User) req.getSession().getAttribute("user")).getUserId();
        WorkoutDAO dao = new WorkoutDAO();

        DateTimeFormatter fullFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek;

        // Parse from weekRange if exists
        String selectedRange = req.getParameter("weekRange");
        if (selectedRange != null && selectedRange.contains(" - ")) {
            try {
                String[] parts = selectedRange.split(" - ");
                startOfWeek = LocalDate.parse(parts[0].trim(), fullFmt);
            } catch (Exception e) {
                startOfWeek = today.with(DayOfWeek.MONDAY);
            }
        } else {
            startOfWeek = today.with(DayOfWeek.MONDAY);
        }

        LocalDate endOfWeek = startOfWeek.plusDays(6);
        String currentWeekRange = fullFmt.format(startOfWeek) + " - " + fullFmt.format(endOfWeek);

        // Week dates (as java.util.Date for JSP fmt:formatDate)
        List<Date> weekDates = new ArrayList<>();
        ZoneId zone = ZoneId.systemDefault();
        for (int i = 0; i < 7; i++) {
            LocalDate d = startOfWeek.plusDays(i);
            Instant instant = d.atStartOfDay(zone).toInstant();
            weekDates.add(Date.from(instant));
        }

        // Generate next 4 weeks' full-range options
        List<String> weekOptions = new ArrayList<>();
        for (int i = -5; i < 5; i++) {
            LocalDate s = today.with(DayOfWeek.MONDAY).plusWeeks(i);
            LocalDate e = s.plusDays(6);
            weekOptions.add(fullFmt.format(s) + " - " + fullFmt.format(e));
        }

        Map<LocalDate, List<Workout>> scheduleMap = dao.getScheduleMap(customerId);

        req.setAttribute("weekDates", weekDates);
        req.setAttribute("scheduleMap", scheduleMap);
        req.setAttribute("currentWeekRange", currentWeekRange);
        req.setAttribute("weekOptions", weekOptions);

        req.getRequestDispatcher("timetable.jsp").forward(req, resp);
    }
}
