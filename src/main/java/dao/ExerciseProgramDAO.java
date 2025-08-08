package dao;

import connectDB.ConnectDatabase;
import model.ExerciseLibrary;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExerciseProgramDAO {

    /**
     * Thêm các bài tập vào ExerciseProgram
     * @param programId ID của chương trình
     * @param exerciseLibraryIds Danh sách ID của ExerciseLibrary
     * @param trainerId ID của huấn luyện viên
     * @return true nếu thêm thành công, false nếu thất bại
     */
    public boolean addExercisesToProgram(int programId, List<Integer> exerciseLibraryIds, int trainerId) {
        String sql = "INSERT INTO ExerciseProgram (ProgramID, ExerciseLibraryID, TrainerID) VALUES (?, ?, ?)";
        try (Connection conn = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (Integer exerciseId : exerciseLibraryIds) {
                ps.setInt(1, programId);
                ps.setInt(2, exerciseId);
                ps.setInt(3, trainerId);
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
     * Lấy danh sách bài tập theo ProgramID
     * @param programId ID của chương trình
     * @return Danh sách ExerciseLibrary
     */
    public List<ExerciseLibrary> getExercisesByProgram(int programId) {
        List<ExerciseLibrary> exercises = new ArrayList<>();
        String sql = "SELECT el.ExerciseLibraryID, el.ExerciseName, el.Sets, el.Reps, el.RestTimeSeconds, " +
                "el.VideoUrl, el.Description, el.MuscleGroup, el.Equipment, el.TrainerID " +
                "FROM ExerciseProgram ep " +
                "JOIN ExerciseLibrary el ON ep.ExerciseLibraryID = el.ExerciseLibraryID " +
                "WHERE ep.ProgramID = ?";
        try (Connection conn = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, programId);
            try (ResultSet rs = ps.executeQuery()) {
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
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return exercises;
    }

    /**
     * Lấy danh sách bài tập có sẵn (không thuộc chương trình) của huấn luyện viên
     * @param programId ID của chương trình
     * @param trainerId ID của huấn luyện viên
     * @return Danh sách ExerciseLibrary
     */
    public List<ExerciseLibrary> getAvailableExercises(int programId, int trainerId) {
        List<ExerciseLibrary> exercises = new ArrayList<>();
        String sql = "SELECT el.ExerciseLibraryID, el.ExerciseName, el.Sets, el.Reps, el.RestTimeSeconds, " +
                "el.VideoUrl, el.Description, el.MuscleGroup, el.Equipment, el.TrainerID " +
                "FROM ExerciseLibrary el " +
                "WHERE el.TrainerID = ? AND el.ExerciseLibraryID NOT IN (" +
                "    SELECT ExerciseLibraryID FROM ExerciseProgram WHERE ProgramID = ?" +
                ")";
        try (Connection conn = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, trainerId);
            ps.setInt(2, programId);
            try (ResultSet rs = ps.executeQuery()) {
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
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return exercises;
    }
}