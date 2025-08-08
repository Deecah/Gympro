package dao;

import connectDB.ConnectDatabase;
import model.Exercise;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExerciseDAO {

    /**
     * Thêm danh sách bài tập vào Exercise
     * @param exercises Danh sách Exercise
     * @return true nếu thêm thành công, false nếu thất bại
     */
    public boolean addExercises(List<Exercise> exercises) {
        String sql = "INSERT INTO Exercise (WorkoutID, ExerciseProgramID) VALUES (?, ?)";
        try (Connection conn = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (Exercise exercise : exercises) {
                ps.setInt(1, exercise.getWorkoutID());
                ps.setInt(2, exercise.getExerciseProgramID());
                ps.addBatch();
            }
            int[] results = ps.executeBatch();
            for (int result : results) {
                if (result == PreparedStatement.EXECUTE_FAILED) {
                    return false;
                }
            }
            return true;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Thêm một bài tập vào Exercise
     * @param exercise Đối tượng Exercise
     * @return ExerciseID của bản ghi vừa thêm, hoặc -1 nếu thất bại
     */
    public int addExercise(Exercise exercise) {
        String sql = "INSERT INTO Exercise (WorkoutID, ExerciseProgramID) VALUES (?, ?)";
        try (Connection conn = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, exercise.getWorkoutID());
            ps.setInt(2, exercise.getExerciseProgramID());
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Lấy danh sách bài tập theo WorkoutID
     * @param workoutId ID của workout
     * @return Danh sách Exercise
     */
    public List<Exercise> getExercisesByWorkoutId(int workoutId) {
        List<Exercise> list = new ArrayList<>();
        String sql = "SELECT e.ExerciseID, e.WorkoutID, e.ExerciseProgramID, el.ExerciseName, el.Sets, el.Reps, " +
                "el.RestTimeSeconds, el.VideoUrl, el.Description, el.MuscleGroup, el.Equipment " +
                "FROM Exercise e " +
                "JOIN ExerciseProgram ep ON e.ExerciseProgramID = ep.ExerciseProgramID " +
                "JOIN ExerciseLibrary el ON ep.ExerciseLibraryID = el.ExerciseLibraryID " +
                "WHERE e.WorkoutID = ?";
        try (Connection conn = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, workoutId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Exercise exercise = new Exercise();
                    exercise.setExerciseID(rs.getInt("ExerciseID"));
                    exercise.setWorkoutID(rs.getInt("WorkoutID"));
                    exercise.setExerciseProgramID(rs.getInt("ExerciseProgramID"));
                    list.add(exercise);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }
}