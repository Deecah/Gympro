package dao;

import connectDB.ConnectDatabase;
import model.CustomerWorkoutSchedule;
import model.ExerciseProgram;
import model.Workout;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TimetableDAO {

    public List<CustomerWorkoutSchedule> getSchedulesForWeek(int customerProgramId, String weekStartDate) {
        List<CustomerWorkoutSchedule> schedules = new ArrayList<>();
        String sql = "SELECT cws.*, w.WorkoutID, w.Date, w.StartTime, w.EndTime, w.Status as WorkoutStatus, p.Name AS ProgramName, u.Name AS CustomerName, cp.ProgramID " +
                "FROM CustomerWorkoutSchedule cws " +
                "JOIN CustomerProgram cp ON cws.CustomerProgramID = cp.Id " +
                "JOIN Program p ON cp.ProgramID = p.ProgramID " +
                "JOIN Users u ON cp.CustomerID = u.Id " +
                "LEFT JOIN Workout w ON cws.ScheduleID = w.ScheduleID " +
                "WHERE cws.CustomerProgramID = ? AND w.Date BETWEEN ? AND DATEADD(day, 6, ?)";
        try (Connection conn = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerProgramId);
            ps.setDate(2, Date.valueOf(weekStartDate));
            ps.setDate(3, Date.valueOf(weekStartDate));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CustomerWorkoutSchedule schedule = new CustomerWorkoutSchedule();
                    schedule.setScheduleId(rs.getInt("ScheduleID"));
                    schedule.setCustomerProgramId(rs.getInt("CustomerProgramID"));
                    schedule.setProgramId(rs.getInt("ProgramID"));
                    schedule.setStartAt(rs.getDate("StartAt"));
                    schedule.setEndAt(rs.getDate("EndAt"));
                    schedule.setStatus(rs.getString("WorkoutStatus"));
                    schedule.setProgramName(rs.getString("ProgramName"));
                    schedule.setCustomerName(rs.getString("CustomerName"));
                    schedule.setWorkoutId(rs.getInt("WorkoutID"));
                    schedule.setDate(rs.getDate("Date"));
                    schedule.setStartTime(rs.getTime("StartTime"));
                    schedule.setEndTime(rs.getTime("EndTime"));
                    List<ExerciseProgram> exercises = getExercisesForWorkout(rs.getInt("WorkoutID"));
                    schedule.setExercises(exercises);
                    schedules.add(schedule);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return schedules;
    }

    private List<ExerciseProgram> getExercisesForWorkout(int workoutId) {
        List<ExerciseProgram> exercises = new ArrayList<>();
        String sql = "SELECT ep.ExerciseProgramID, ep.ExerciseLibraryID, ep.TrainerID, ep.ProgramID, " +
                "el.ExerciseName, el.Sets, el.Reps, el.RestTimeSeconds, el.VideoUrl, el.Description, el.MuscleGroup, el.Equipment " +
                "FROM Exercise e " +
                "JOIN ExerciseProgram ep ON e.ExerciseProgramID = ep.ExerciseProgramID " +
                "JOIN ExerciseLibrary el ON ep.ExerciseLibraryID = el.ExerciseLibraryID " +
                "WHERE e.WorkoutID = ?";
        try (Connection conn = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, workoutId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ExerciseProgram exercise = new ExerciseProgram();
                    exercise.setExerciseProgramID(rs.getInt("ExerciseProgramID"));
                    exercise.setExerciseLibraryID(rs.getInt("ExerciseLibraryID"));
                    exercise.setTrainerID(rs.getInt("TrainerID"));
                    exercise.setProgramID(rs.getInt("ProgramID"));
                    exercise.setExerciseName(rs.getString("ExerciseName"));
                    exercise.setSets(rs.getInt("Sets"));
                    exercise.setReps(rs.getInt("Reps"));
                    exercise.setRestTimeSeconds(rs.getInt("RestTimeSeconds"));
                    exercise.setVideoUrl(rs.getString("VideoUrl"));
                    exercise.setDescription(rs.getString("Description"));
                    exercise.setMuscleGroup(rs.getString("MuscleGroup"));
                    exercise.setEquipment(rs.getString("Equipment"));
                    exercises.add(exercise);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return exercises;
    }

    public List<ExerciseProgram> getExerciseProgramsByProgramId(int programId) {
        List<ExerciseProgram> programs = new ArrayList<>();
        String sql = "SELECT ep.ExerciseProgramID, ep.ExerciseLibraryID, ep.TrainerID, ep.ProgramID, " +
                "el.ExerciseName, el.Sets, el.Reps, el.RestTimeSeconds, el.VideoUrl, el.Description, el.MuscleGroup, el.Equipment " +
                "FROM ExerciseProgram ep " +
                "JOIN ExerciseLibrary el ON ep.ExerciseLibraryID = el.ExerciseLibraryID " +
                "WHERE ep.ProgramID = ?";
        try (Connection conn = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, programId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ExerciseProgram program = new ExerciseProgram();
                    program.setExerciseProgramID(rs.getInt("ExerciseProgramID"));
                    program.setExerciseLibraryID(rs.getInt("ExerciseLibraryID"));
                    program.setTrainerID(rs.getInt("TrainerID"));
                    program.setProgramID(rs.getInt("ProgramID"));
                    program.setExerciseName(rs.getString("ExerciseName"));
                    program.setSets(rs.getInt("Sets"));
                    program.setReps(rs.getInt("Reps"));
                    program.setRestTimeSeconds(rs.getInt("RestTimeSeconds"));
                    program.setVideoUrl(rs.getString("VideoUrl"));
                    program.setDescription(rs.getString("Description"));
                    program.setMuscleGroup(rs.getString("MuscleGroup"));
                    program.setEquipment(rs.getString("Equipment"));
                    programs.add(program);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return programs;
    }

    public void addWorkoutAndSchedule(int scheduleId, Workout workout, List<Integer> exerciseProgramIds) {
        Connection conn = null;
        try {
            conn = ConnectDatabase.getInstance().openConnection();
            conn.setAutoCommit(false);

            // Thêm CustomerWorkoutSchedule nếu scheduleId chưa tồn tại
            if (scheduleId == -1) {
                CustomerWorkoutScheduleDAO scheduleDAO = new CustomerWorkoutScheduleDAO();
                CustomerWorkoutSchedule schedule = new CustomerWorkoutSchedule();
                schedule.setCustomerProgramId(workout.getCustomerProgramId());
                schedule.setStartAt(workout.getDate());
                schedule.setEndAt(workout.getDate());
                schedule.setStatus("pending");
                scheduleId = scheduleDAO.addSchedule(schedule);
            }

            // Thêm Workout
            String workoutSql = "INSERT INTO Workout (Date, StartTime, EndTime, TrainerID, Status, CustomerProgramID, ScheduleID) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(workoutSql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setDate(1, workout.getDate());
                ps.setTime(2, workout.getStartTime());
                ps.setTime(3, workout.getEndTime());
                ps.setInt(4, workout.getTrainerId());
                ps.setString(5, workout.getStatus());
                ps.setInt(6, workout.getCustomerProgramId());
                ps.setInt(7, scheduleId);
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        workout.setWorkoutId(rs.getInt(1));
                    }
                }
            }

            // Thêm Exercise
            String exerciseSql = "INSERT INTO Exercise (WorkoutID, ExerciseProgramID) VALUES (?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(exerciseSql)) {
                for (Integer exerciseProgramId : exerciseProgramIds) {
                    ps.setInt(1, workout.getWorkoutId());
                    ps.setInt(2, exerciseProgramId);
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            conn.commit();
        } catch (SQLException | ClassNotFoundException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void updateScheduleStatus(int scheduleId, String status) {
        CustomerWorkoutScheduleDAO scheduleDAO = new CustomerWorkoutScheduleDAO();
        scheduleDAO.updateStatus(scheduleId, status);
    }

    public Object getWorkoutDetail(int workoutId) {
        // Cập nhật để lấy thông tin từ ExerciseLibrary
        return null; // Giữ nguyên vì không ảnh hưởng trực tiếp
    }

    public boolean isValidTrainerForProgram(int trainerId, int customerProgramId) {
        String sql = "SELECT COUNT(*) FROM CustomerProgram cp " +
                "JOIN Contracts c ON cp.CustomerID = c.CustomerID " +
                "WHERE cp.Id = ? AND c.TrainerID = ?";
        try (Connection conn = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerProgramId);
            ps.setInt(2, trainerId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
}