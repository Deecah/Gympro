package dao;

import connectDB.ConnectDatabase;
import model.Exercise;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ExerciseDAO {
    public boolean addExercises(List<Exercise> exercises) {
        String sql = "INSERT INTO Exercise (WorkoutID, ExerciseLibraryID) " +
                "VALUES (?, ?)";
        try (Connection conn = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (Exercise exercise : exercises) {
                ps.setInt(1, exercise.getWorkoutID());
                ps.setInt(2, exercise.getExerciseLibraryID());
                ps.addBatch();
            }
            int[] results = ps.executeBatch();
            for (int result : results) {
                if (result == PreparedStatement.EXECUTE_FAILED) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public int addExercise(Exercise ex) {
        String sql = "INSERT INTO Exercise (WorkoutID, ExerciseLibraryID) VALUES (?, ?)";
        try (Connection con = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, ex.getWorkoutID());
            ps.setInt(2, ex.getExerciseLibraryID());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public List<Exercise> getExercisesByWorkoutId(int workoutID) {
        List<Exercise> list = new ArrayList<>();
        String sql = "SELECT e.* " +
                "FROM Exercise e JOIN ExerciseLibrary el ON e.ExerciseLibraryID = el.ExerciseLibraryID WHERE e.WorkoutID = ?";
        try (Connection con = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, workoutID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Exercise ex = new Exercise();
                    ex.setExerciseID(rs.getInt("ExerciseID"));
                    ex.setWorkoutID(rs.getInt("WorkoutID"));
                    ex.setExerciseLibraryID(rs.getInt("ExerciseLibraryID"));
                    list.add(ex);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}