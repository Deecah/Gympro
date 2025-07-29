package dao;

import connectDB.ConnectDatabase;
import model.ExerciseLibrary;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExerciseLibraryDAO {

    public boolean insertExercise(ExerciseLibrary exercise) {
        String sql = "INSERT INTO ExerciseLibrary (Name, Description, VideoURL, MuscleGroup, Equipment) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, exercise.getName());
            ps.setString(2, exercise.getDescription());
            ps.setString(3, exercise.getVideoURL());
            ps.setString(4, exercise.getMuscleGroup());
            ps.setString(5, exercise.getEquipment());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<ExerciseLibrary> getAllExercises() {
        List<ExerciseLibrary> list = new ArrayList<>();
        String sql = "SELECT * FROM ExerciseLibrary";

        try (Connection conn = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ExerciseLibrary ex = new ExerciseLibrary();
                ex.setExerciseID(rs.getInt("ExerciseID"));
                ex.setName(rs.getString("Name"));
                ex.setDescription(rs.getString("Description"));
                ex.setVideoURL(rs.getString("VideoURL"));
                ex.setMuscleGroup(rs.getString("MuscleGroup"));
                ex.setEquipment(rs.getString("Equipment"));
                list.add(ex);
            }
            System.out.println("Successfully retrieved " + list.size() + " exercises from database");

        } catch (Exception e) {
            System.err.println("Error in getAllExercises: " + e.getMessage());
            e.printStackTrace();
        }

        return list;
    }
    public ExerciseLibrary getExerciseById(int id) {
    String sql = "SELECT * FROM ExerciseLibrary WHERE ExerciseID = ?";
    try (Connection conn = ConnectDatabase.getInstance().openConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            ExerciseLibrary ex = new ExerciseLibrary();
            ex.setExerciseID(rs.getInt("ExerciseID"));
            ex.setName(rs.getString("Name"));
            ex.setDescription(rs.getString("Description"));
            ex.setVideoURL(rs.getString("VideoURL"));
            ex.setMuscleGroup(rs.getString("MuscleGroup"));
            ex.setEquipment(rs.getString("Equipment"));
            return ex;
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}
    public boolean updateExercise(ExerciseLibrary exercise) {
    String sql = "UPDATE ExerciseLibrary SET Name=?, Description=?, VideoURL=?, MuscleGroup=?, Equipment=? WHERE ExerciseID=?";

    try (Connection conn = ConnectDatabase.getInstance().openConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, exercise.getName());
        ps.setString(2, exercise.getDescription());
        ps.setString(3, exercise.getVideoURL());
        ps.setString(4, exercise.getMuscleGroup());
        ps.setString(5, exercise.getEquipment());
        ps.setInt(6, exercise.getExerciseID());

        return ps.executeUpdate() > 0;

    } catch (Exception e) {
        e.printStackTrace();
    }

    return false;
}
    public boolean deleteExercise(int id) {
        String sql = "DELETE FROM ExerciseLibrary WHERE ExerciseID = ?";
        try (Connection conn = ConnectDatabase.getInstance().openConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}