package controller;

import dao.WorkoutDAO;
import dao.ProgressDAO;
import model.WorkoutSlotDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.User;

import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

@WebServlet("/timetable")
public class TimetableServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        int customerId = user.getUserId();
        String role = user.getRole();
        ZoneId zone = ZoneId.systemDefault();
        DateTimeFormatter fullFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate today = LocalDate.now();

        // Xác định tuần đang chọn hoặc mặc định là tuần hiện tại
        LocalDate startOfWeek;
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

        // Tạo danh sách tuần trong năm
        List<String> weekOptions = new ArrayList<>();
        LocalDate firstMonday = LocalDate.of(today.getYear(), 1, 4).with(DayOfWeek.MONDAY);
        for (int i = 0; i < 52; i++) {
            LocalDate s = firstMonday.plusWeeks(i);
            LocalDate e = s.plusDays(6);
            weekOptions.add(fullFmt.format(s) + " - " + fullFmt.format(e));
        }
        if (!weekOptions.contains(currentWeekRange)) {
            weekOptions.add(0, currentWeekRange);
        }

        // Lấy dữ liệu lịch theo slotId
        WorkoutDAO dao = new WorkoutDAO();
        Map<Integer, List<WorkoutSlotDTO>> slotMap =
                dao.getSlotSchedule(customerId, startOfWeek, endOfWeek, role);

        // Bổ sung trạng thái completed cho từng slot
        ProgressDAO progressDAO = new ProgressDAO();
        for (List<WorkoutSlotDTO> slots : slotMap.values()) {
            for (WorkoutSlotDTO slot : slots) {
                if (slot.getWorkoutId() > 0) {
                    boolean completed = progressDAO.isWorkoutCompleted(customerId, slot.getWorkoutId());
                    slot.setCompleted(completed);
                }
            }
        }

        // Gửi dữ liệu sang JSP
        req.setAttribute("slotMap", slotMap);
        req.setAttribute("weekOptions", weekOptions);
        req.setAttribute("currentWeekRange", currentWeekRange);
        
        // Xử lý thông báo từ MarkCompletedServlet
        String msg = req.getParameter("msg");
        if (msg != null) {
            req.setAttribute("message", msg);
        }

        req.getRequestDispatcher("timetable.jsp").forward(req, resp);
    }
}
