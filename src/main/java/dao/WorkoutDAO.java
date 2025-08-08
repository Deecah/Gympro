package dao;

import connectDB.ConnectDatabase;
import model.Workout;
import model.ExerciseLibrary;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkoutDAO {

    /**
     * Lấy danh sách workout theo ScheduleID
     * @param scheduleId ID của lịch trình
     * @return Map với key là Date và value là danh sách Workout
     */
    public Map<Date, List<Workout>> getWorkoutsByProgram(int scheduleId) {
        Map<Date, List<Workout>> workoutsMap = new HashMap<>();
        String sql = "SELECT w.WorkoutID, w.Date, w.StartTime, w.EndTime, w.Status, w.CustomerProgramID, w.ScheduleID, w.TrainerID " +
                "FROM Workout w " +
                "WHERE w.ScheduleID = ?";
        try (Connection conn = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, scheduleId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Workout workout = new Workout();
                    workout.setWorkoutId(rs.getInt("WorkoutID"));
                    workout.setDate(rs.getDate("Date"));
                    workout.setStartTime(rs.getTime("StartTime"));
                    workout.setEndTime(rs.getTime("EndTime"));
                    workout.setCustomerProgramId(rs.getInt("CustomerProgramID"));
                    workout.setScheduleId(rs.getInt("ScheduleID"));
                    workout.setStatus(rs.getString("Status"));
                    workout.setTrainerId(rs.getInt("TrainerID"));
                    workoutsMap.computeIfAbsent(workout.getDate(), k -> new ArrayList<>()).add(workout);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return workoutsMap;
    }

    /**
     * Lấy danh sách bài tập theo danh sách workout
     * @param dayWorkouts Map chứa danh sách workout theo ngày
     * @return Map với key là WorkoutID và value là danh sách ExerciseLibrary
     */
    public Map<Integer, List<ExerciseLibrary>> getExercisesByWorkouts(Map<Date, List<Workout>> dayWorkouts) {
        Map<Integer, List<ExerciseLibrary>> exercisesMap = new HashMap<>();
        String sql = "SELECT e.WorkoutID, el.ExerciseLibraryID, el.ExerciseName, el.Sets, el.Reps, " +
                "el.RestTimeSeconds, el.VideoUrl, el.Description, el.MuscleGroup, el.Equipment, el.TrainerID " +
                "FROM Exercise e " +
                "JOIN ExerciseProgram ep ON e.ExerciseProgramID = ep.ExerciseProgramID " +
                "JOIN ExerciseLibrary el ON ep.ExerciseLibraryID = el.ExerciseLibraryID " +
                "WHERE e.WorkoutID = ?";
        try (Connection conn = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (List<Workout> workouts : dayWorkouts.values()) {
                for (Workout workout : workouts) {
                    int workoutId = workout.getWorkoutId();
                    ps.setInt(1, workoutId);
                    try (ResultSet rs = ps.executeQuery()) {
                        List<ExerciseLibrary> exercises = new ArrayList<>();
                        while (rs.next()) {
                            ExerciseLibrary exercise = new ExerciseLibrary();
                            exercise.setExerciseID(rs.getInt("ExerciseLibraryID"));
                            exercise.setName(rs.getString("ExerciseName"));
                            exercise.setSets(rs.getInt("Sets"));
                            exercise.setReps(rs.getInt("Reps"));
                            exercise.setRestTimeSeconds(rs.getInt("RestTimeSeconds"));
                            exercise.setVideoURL(rs.getString("VideoUrl"));
                            exercise.setDescription(rs.getString("Description"));
                            exercise.setMuscleGroup(rs.getString("MuscleGroup"));
                            exercise.setEquipment(rs.getString("Equipment"));
                            exercise.setTrainerID(rs.getInt("TrainerID"));
                            exercises.add(exercise);
                        }
                        exercisesMap.put(workoutId, exercises);
                    }
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return exercisesMap;
    }

    /**
     * Thêm một workout mới
     * @param workout Đối tượng Workout chứa thông tin
     * @return WorkoutID của bản ghi vừa thêm, hoặc -1 nếu thất bại
     */
    public int addWorkout(Workout workout) {
        String sql = "INSERT INTO Workout (Date, StartTime, EndTime, TrainerID, Status, CustomerProgramID, ScheduleID) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDate(1, workout.getDate());
            ps.setTime(2, workout.getStartTime());
            ps.setTime(3, workout.getEndTime());
            ps.setInt(4, workout.getTrainerId());
            ps.setString(5, workout.getStatus());
            ps.setInt(6, workout.getCustomerProgramId());
            ps.setInt(7, workout.getScheduleId());
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Xóa một workout
     * @param workoutId ID của workout cần xóa
     * @return true nếu xóa thành công, false nếu thất bại
     */
    public boolean deleteWorkout(int workoutId) {
        String sql = "DELETE FROM Workout WHERE WorkoutID = ?";
        try (Connection conn = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, workoutId);
            return ps.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean markCompletedWorkout(int workoutId, String status) {
        String sql = "UPDATE Workout SET Status = ? WHERE WorkoutID = ?";
        try (Connection conn = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, workoutId);
            return ps.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}