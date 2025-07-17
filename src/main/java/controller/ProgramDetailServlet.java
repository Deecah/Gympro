package controller;

import dao.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.*;
import model.Package;

import java.io.IOException;
import java.util.*;

@WebServlet(name = "ProgramDetailServlet", urlPatterns = {"/ProgramDetailServlet"})
public class ProgramDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("user") == null) {
                response.sendRedirect(request.getContextPath() + "/login.jsp");
                return;
            }

            User user = (User) session.getAttribute("user");
            int trainerId = user.getUserId();

            String programIdStr = request.getParameter("programId");
            int programId;
            try {
                programId = Integer.parseInt(programIdStr);
            } catch (NumberFormatException e) {
                response.sendError(400, "Invalid programId");
                return;
            }

            ProgramDAO programDAO = new ProgramDAO();
            Program program = programDAO.getProgramById(programId, trainerId);

            if (program == null) {
                response.sendError(404, "Program not found");
                return;
            }

            ProgramWeekDAO weekDAO = new ProgramWeekDAO();
            ProgramDayDAO dayDAO = new ProgramDayDAO();
            WorkoutDAO workoutDAO = new WorkoutDAO();
            ExerciseLibraryDAO exerciseLibraryDAO = new ExerciseLibraryDAO();
            PackageDAO packageDAO = new PackageDAO();

            List<ProgramWeek> weeks = weekDAO.getWeeksByProgramId(programId);
            Map<Integer, List<ProgramDay>> daysMap = new HashMap<>();
            Map<Integer, List<Workout>> dayWorkouts = new HashMap<>();

            for (ProgramWeek week : weeks) {
                List<ProgramDay> days = dayDAO.getDaysByWeekId(week.getWeekId());
                daysMap.put(week.getWeekId(), days);
                for (ProgramDay day : days) {
                    List<Workout> workouts = workoutDAO.getWorkoutsByDayId(day.getDayId());
                    dayWorkouts.put(day.getDayId(), workouts);
                }
            }

            List<ExerciseLibrary> exerciseList = exerciseLibraryDAO.getAllExercises();
            List<Package> packageList = packageDAO.getAllPackagesByTrainer(trainerId);
            request.setAttribute("packageList", packageList);

            // 4. Đẩy dữ liệu sang JSP
            request.setAttribute("program", program);
            request.setAttribute("weeks", weeks);
            request.setAttribute("daysMap", daysMap);
            request.setAttribute("dayWorkouts", dayWorkouts);
            request.setAttribute("exerciseList", exerciseList);
            request.setAttribute("packageList", packageList);

            request.getRequestDispatcher("trainer/program-detail.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace(); // Xem trong console IDE hoặc server log
            response.setContentType("text/plain");
            response.getWriter().write("Lỗi trong ProgramDetailServlet: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");

        int trainerId = user.getUserId();

        try {
            int programId = Integer.parseInt(request.getParameter("programId"));
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            int packageId = Integer.parseInt(request.getParameter("packageId"));

            Program program = new Program();
            program.setProgramId(programId);
            program.setName(name);
            program.setDescription(description);
            program.setPackageId(packageId);

            new ProgramDAO().updateProgram(program, trainerId);

            // ✅ Sau khi update xong, redirect lại trang chi tiết
            response.sendRedirect(request.getContextPath() + "/ProgramDetailServlet?programId=" + programId);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(500, "Failed to update program");
        }
    }

}
