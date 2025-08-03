package controller;

import dao.TimetableDAO;
import model.CustomerWorkoutSchedule;
import model.ExerciseProgram;
import model.Workout;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/TimetableServlet")
public class TimetableServlet extends HttpServlet {

    private TimetableDAO timetableDAO = new TimetableDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int customerProgramId = Integer.parseInt(request.getParameter("customerProgramId"));
        String weekStartDate = request.getParameter("weekStartDate"); // yyyy-MM-dd
        int programId = Integer.parseInt(request.getParameter("programId")); // Giả định lấy từ CustomerProgram

        // Lấy danh sách lịch trình
        List<CustomerWorkoutSchedule> schedules = timetableDAO.getSchedulesForWeek(customerProgramId, weekStartDate);

        // Lấy danh sách ExerciseProgram
        List<ExerciseProgram> exercisePrograms = timetableDAO.getExerciseProgramsByProgramId(programId);

        request.setAttribute("schedules", schedules);
        request.setAttribute("exercisePrograms", exercisePrograms);
        request.getRequestDispatcher("trainer/timetable.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("addWorkout".equals(action)) {
            // Lấy thông tin từ form
            int dayId = Integer.parseInt(request.getParameter("dayId"));
            String startTimeStr = request.getParameter("startTime");
            String endTimeStr = request.getParameter("endTime");
            String title = request.getParameter("title");
            String notes = request.getParameter("notes");
            int trainerId = Integer.parseInt(request.getParameter("trainerId"));
            int customerProgramId = Integer.parseInt(request.getParameter("customerProgramId"));
            int programDayId = Integer.parseInt(request.getParameter("programDayId"));
            LocalDate scheduledDate = LocalDate.parse(request.getParameter("scheduledDate"));
            String[] exerciseProgramIds = request.getParameterValues("exerciseProgramIds");
            String status = "pending";

            // Chuyển đổi thời gian
            LocalTime startTime = LocalTime.parse(startTimeStr);
            LocalTime endTime = LocalTime.parse(endTimeStr);

            // Tạo đối tượng Workout
            Workout workout = new Workout();
            workout.setDayId(dayId);
            workout.setStartTime(Time.valueOf(startTime));
            workout.setEndTime(Time.valueOf(endTime));
            workout.setTitle(title);
            workout.setNotes(notes);
            workout.setTrainerId(trainerId);

            // Tạo đối tượng CustomerWorkoutSchedule
            CustomerWorkoutSchedule schedule = new CustomerWorkoutSchedule();
            schedule.setCustomerProgramId(customerProgramId);
            schedule.setProgramDayId(programDayId);
            schedule.setScheduledDate(scheduledDate);
            schedule.setStartTime(startTime);
            schedule.setEndTime(endTime);
            schedule.setStatus(status);

            // Chuyển đổi exerciseProgramIds thành danh sách
            List<Integer> exerciseProgramIdList = Arrays.stream(exerciseProgramIds)
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());

            // Thêm workout và schedule
            int scheduleId = timetableDAO.addWorkoutAndSchedule(workout, schedule, exerciseProgramIdList);
            if (scheduleId != -1) {
                response.getWriter().write("Workout added successfully with ScheduleID: " + scheduleId);
            } else {
                response.getWriter().write("Failed to add workout");
            }
        } else if ("updateStatus".equals(action)) {
            int scheduleId = Integer.parseInt(request.getParameter("scheduleId"));
            String status = request.getParameter("status");
            timetableDAO.updateScheduleStatus(scheduleId, status);
            response.getWriter().write("Status updated successfully");
        }
    }
}