package dao;

import connectDB.ConnectDatabase;
import model.ExerciseLibrary;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExerciseLibraryDAO {

    /**
     * Thêm một bài tập vào ExerciseLibrary
     * @param exercise Đối tượng ExerciseLibrary
     * @return ExerciseLibraryID của bản ghi vừa thêm, hoặc -1 nếu thất bại
     */
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
     * Lấy danh sách tất cả bài tập theo TrainerID
     * @param trainerId ID của huấn luyện viên
     * @return Danh sách ExerciseLibrary
     */
    public List<ExerciseLibrary> getAllExercises(int trainerId) {
        List<ExerciseLibrary> exercises = new ArrayList<>();
        String sql = "SELECT ExerciseLibraryID, ExerciseName, Sets, Reps, RestTimeSeconds, VideoUrl, " +
                "Description, MuscleGroup, Equipment, TrainerID " +
                "FROM ExerciseLibrary WHERE TrainerID = ?";
        try (Connection conn = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, trainerId);
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
     * Lấy bài tập theo ExerciseLibraryID
     * @param id ID của bài tập
     * @return Đối tượng ExerciseLibrary hoặc null nếu không tìm thấy
     */
    public ExerciseLibrary getExerciseById(int id) {
        String sql = "SELECT * FROM ExerciseLibrary WHERE ExerciseLibraryID = ?";
        try (Connection conn = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ExerciseLibrary exercise = new ExerciseLibrary();
                    exercise.setExerciseID(rs.getInt("ExerciseLibraryID"));
                    exercise.setName(rs.getString("ExerciseName"));
                    exercise.setDescription(rs.getString("Description"));
                    exercise.setVideoURL(rs.getString("VideoUrl"));
                    exercise.setMuscleGroup(rs.getString("MuscleGroup"));
                    exercise.setEquipment(rs.getString("Equipment"));
                    exercise.setSets(rs.getInt("Sets"));
                    exercise.setReps(rs.getInt("Reps"));
                    exercise.setRestTimeSeconds(rs.getInt("RestTimeSeconds"));
                    exercise.setTrainerID(rs.getInt("TrainerID"));
                    return exercise;
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Cập nhật bài tập
     * @param exercise Đối tượng ExerciseLibrary
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    public boolean updateExercise(ExerciseLibrary exercise) {
        String sql = "UPDATE ExerciseLibrary SET ExerciseName = ?, Description = ?, VideoUrl = ?, MuscleGroup = ?, Equipment = ?, Sets = ?, Reps = ?, RestTimeSeconds = ? WHERE ExerciseLibraryID = ?";
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
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Xóa bài tập
     * @param id ID của bài tập
     * @return true nếu xóa thành công, false nếu thất bại
     */
    public boolean deleteExercise(int id) {
        String sql = "DELETE FROM ExerciseLibrary WHERE ExerciseLibraryID = ?";
        try (Connection conn = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}