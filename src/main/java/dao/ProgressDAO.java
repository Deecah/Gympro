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

}
