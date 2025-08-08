//package controller;
//
//import dao.ProgramDayDAO;
//import dao.WorkoutDAO;
//import dao.ExerciseDAO;
//import model.Workout;
//import model.Exercise;
//import model.User;
//import scheduler.ScheduleWorkoutJob;
//
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.*;
//import java.io.IOException;
//import java.sql.Time;
//import java.time.LocalTime;
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//
//@WebServlet("/AddWorkoutToScheduleServlet")
//public class AddWorkoutToScheduleServlet extends HttpServlet {
//    private WorkoutDAO workoutDAO = new WorkoutDAO();
//    private ProgramDayDAO programDayDAO = new ProgramDayDAO();
//    private ExerciseDAO exerciseDAO = new ExerciseDAO();
//
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        HttpSession session = request.getSession();
//        User user = (User) session.getAttribute("user");
//        if (user == null || !"trainer".equalsIgnoreCase(user.getRole())) {
//            response.sendRedirect("login.jsp");
//            return;
//        }
//
//        // Validate inputs
//        String programIdStr = request.getParameter("programId");
//        String dateStr = request.getParameter("workoutDate");
//        String startTimeStr = request.getParameter("startTime");
//        String endTimeStr = request.getParameter("endTime");
//        String[] exerciseLibraryIds = request.getParameterValues("exerciseLibraryIds");
//
//        if (programIdStr == null || dateStr == null || startTimeStr == null || endTimeStr == null || title == null || exerciseLibraryIds == null || exerciseLibraryIds.length == 0) {
//            response.sendRedirect("ProgramServlet?programId=" + programIdStr + "&error=Missing required parameters");
//            return;
//        }
//
//        int programId;
//        LocalDate workoutDate;
//        LocalTime startTime;
//        LocalTime endTime;
//        try {
//            programId = Integer.parseInt(programIdStr);
//            workoutDate = LocalDate.parse(dateStr);
//            startTime = LocalTime.parse(startTimeStr);
//            endTime = LocalTime.parse(endTimeStr);
//            if (startTime.isAfter(endTime) || startTime.equals(endTime)) {
//                response.sendRedirect("ProgramServlet?programId=" + programId + "&error=Start time must be before end time");
//                return;
//            }
//        } catch (Exception e) {
//            response.sendRedirect("ProgramServlet?programId=" + programIdStr + "&error=Invalid input format");
//            return;
//        }
//
//        // Get or create ProgramDay
//        int dayId = programDayDAO.getOrCreateDayId(programId, dateStr);
//        if (dayId == -1) {
//            response.sendRedirect("ProgramServlet?programId=" + programId + "&error=Failed to get or create program day");
//            return;
//        }
//
//        // Create Workout
//        Workout workout = new Workout();
//        workout.setDayId(dayId);
//        workout.setTitle(title);
//        workout.setNotes(notes);
//        workout.setTrainerId(user.getUserId());
//        workout.setStartTime(Time.valueOf(startTime));
//        workout.setEndTime(Time.valueOf(endTime));
//
//        // Add Workout to database
//        int workoutId = workoutDAO.addWorkout(workout);
//        if (workoutId == -1) {
//            response.sendRedirect("ProgramServlet?programId=" + programId + "&error=Failed to add workout");
//            return;
//        }
//
//        // Add Exercises to database
//        List<Exercise> exercises = new ArrayList<>();
//        for (int i = 0; i < exerciseLibraryIds.length; i++) {
//            try {
//                Exercise exercise = new Exercise();
//                exercise.setWorkoutID(workoutId);
//                exercise.setExerciseLibraryID(Integer.parseInt(exerciseLibraryIds[i]));
//                exercises.add(exercise);
//            } catch (NumberFormatException e) {
//                workoutDAO.deleteWorkout(workoutId);
//                response.sendRedirect("ProgramServlet?programId=" + programId + "&error=Invalid exercise parameters");
//                return;
//            }
//        }
//
//        boolean exerciseSuccess = exerciseDAO.addExercises(exercises);
//        if (!exerciseSuccess) {
//            workoutDAO.deleteWorkout(workoutId);
//            response.sendRedirect("ProgramServlet?programId=" + programId + "&error=Failed to add exercises");
//            return;
//        }
//
//        // Schedule the workout
//        try {
//            ScheduleWorkoutJob.schedule(workout, dateStr, startTimeStr);
//            response.sendRedirect("ProgramServlet?programId=" + programId + "&success=Workout and exercises added successfully");
//        } catch (Exception e) {
//            e.printStackTrace();
//            workoutDAO.deleteWorkout(workoutId);
//            response.sendRedirect("ProgramServlet?programId=" + programId + "&error=Failed to schedule workout");
//        }
//    }
//}