package dao;

import connectDB.ConnectDatabase;
import model.ExerciseLibrary;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ExerciseLibraryDAO {

    public int insertExercise(ExerciseLibrary exercise) {
        String sql = "INSERT INTO ExerciseLibrary (ExerciseName, Description, VideoUrl, MuscleGroup, Equipment, Sets, Reps, RestTimeSeconds, TrainerID) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, exercise.getName());
            ps.setString(2, exercise.getDescription());
            ps.setString(3, exercise.getVideoURL());
            ps.setString(4, exercise.getMuscleGroup());
            ps.setString(5, exercise.getEquipment());
            ps.setInt(6, exercise.getSets());
            ps.setInt(7, exercise.getReps());
            ps.setInt(8, exercise.getRestTimeSeconds());
            ps.setInt(9, exercise.getTrainerID());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public List<ExerciseLibrary> getAllExercises(int trainerId) {
        List<ExerciseLibrary> exercises = new ArrayList<>();
        String sql = "SELECT ExerciseLibraryID, ExerciseName, Sets, Reps, RestTimeSeconds, VideoUrl, " +
                "Description, MuscleGroup, Equipment, TrainerID " +
                "FROM ExerciseLibrary WHERE TrainerID = ?";
        try (Connection conn = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, trainerId);
            ResultSet rs = ps.executeQuery();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exercises;
    }

    public ExerciseLibrary getExerciseById(int id) {
        String sql = "SELECT * FROM ExerciseLibrary WHERE ExerciseLibraryID = ?";
        try (Connection conn = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ExerciseLibrary ex = new ExerciseLibrary();
                    ex.setExerciseID(rs.getInt("ExerciseLibraryID"));
                    ex.setName(rs.getString("ExerciseName"));
                    ex.setDescription(rs.getString("Description"));
                    ex.setVideoURL(rs.getString("VideoUrl"));
                    ex.setMuscleGroup(rs.getString("MuscleGroup"));
                    ex.setEquipment(rs.getString("Equipment"));
                    ex.setSets(rs.getInt("Sets"));
                    ex.setReps(rs.getInt("Reps"));
                    ex.setRestTimeSeconds(rs.getInt("RestTimeSeconds"));
                    ex.setTrainerID(rs.getInt("TrainerID"));
                    return ex;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateExercise(ExerciseLibrary exercise) {
        String sql = "UPDATE ExerciseLibrary SET ExerciseName=?, Description=?, VideoUrl=?, MuscleGroup=?, Equipment=?, Sets=?, Reps=?, RestTimeSeconds=? WHERE ExerciseLibraryID=?";
        try (Connection conn = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, exercise.getName());
            ps.setString(2, exercise.getDescription());
            ps.setString(3, exercise.getVideoURL());
            ps.setString(4, exercise.getMuscleGroup());
            ps.setString(5, exercise.getEquipment());
            ps.setInt(6, exercise.getSets());
            ps.setInt(7, exercise.getReps());
            ps.setInt(8, exercise.getRestTimeSeconds());
            ps.setInt(9, exercise.getExerciseID());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteExercise(int id) {
        String sql = "DELETE FROM ExerciseLibrary WHERE ExerciseLibraryID = ?";
        try (Connection conn = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}