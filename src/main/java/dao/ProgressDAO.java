package dao;

import model.Progress;
import connectDB.ConnectDatabase;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProgressDAO {

    public static List<Progress> getProgressByUserID(int userId) {
        List<Progress> list = new ArrayList<>();
        String sql = "SELECT * FROM Progress WHERE UserID = ? ORDER BY RecordedAt DESC";

        try (Connection conn = ConnectDatabase.getInstance().openConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Progress p = new Progress();
                    p.setProgressID(rs.getInt("ProgressID"));
                    p.setUserID(rs.getInt("UserID"));
                    Timestamp ts = rs.getTimestamp("RecordedAt");
                    if (ts != null) {
                        p.setRecordedAt(ts.toLocalDateTime());
                    }
                    p.setWeight(rs.getDouble("weight"));
                    p.setBodyFatPercent(rs.getDouble("body_fat_percent"));
                    p.setMuscleMass(rs.getDouble("muscle_mass"));
                    p.setNotes(rs.getString("Notes"));
                    list.add(p);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public static Progress getProgressById(int progressId) {
        Progress progress = null;
        String sql = "SELECT * FROM Progress WHERE progressID = ?";

        try (Connection conn = ConnectDatabase.getInstance().openConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, progressId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                progress = new Progress();
                progress.setProgressID(rs.getInt("progressID"));
                progress.setUserID(rs.getInt("userID"));
                progress.setWorkoutID(rs.getInt("workoutID"));
                progress.setRecordedAt(rs.getTimestamp("recordedAt").toLocalDateTime());
                progress.setWeight(rs.getDouble("weight"));
                progress.setBodyFatPercent(rs.getDouble("bodyFatPercent"));
                progress.setMuscleMass(rs.getDouble("muscleMass"));
                progress.setNotes(rs.getString("notes"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return progress;
    }

    public static void updateProgress(Progress p) {
        String sql = "UPDATE Progress SET recordedAt = ?, weight = ?, bodyFatPercent = ?, muscleMass = ?, notes = ? WHERE progressID = ?";

        try (Connection conn = ConnectDatabase.getInstance().openConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setTimestamp(1, Timestamp.valueOf(p.getRecordedAt()));
            ps.setDouble(2, p.getWeight());
            ps.setDouble(3, p.getBodyFatPercent());
            ps.setDouble(4, p.getMuscleMass());
            ps.setString(5, p.getNotes());
            ps.setInt(6, p.getProgressID());

            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
