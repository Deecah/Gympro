package dao;

import connectDB.ConnectDatabase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.ProgramWeek;

public class ProgramWeekDAO {

    public static int addProgramWeek(int programId, int weekNumber) {
        String sql = "INSERT INTO ProgramWeek (ProgramID, WeekNumber) VALUES (?, ?)";
        try (Connection con = ConnectDatabase.getInstance().openConnection();
             PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, programId);
            stmt.setInt(2, weekNumber);
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (Exception e) {
            Logger.getLogger(ProgramWeekDAO.class.getName()).log(Level.SEVERE, "Add program week failed", e);
        }
        return -1;
    }

    public static ArrayList<Integer> addDaysForWeek(int weekId) {
        String sql = "INSERT INTO ProgramDay (WeekID, DayNumber) VALUES (?, ?)";
        ArrayList<Integer> dayIds = new ArrayList<>();
        try (Connection con = ConnectDatabase.getInstance().openConnection()) {
            for (int i = 1; i <= 7; i++) {
                try (PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setInt(1, weekId);
                    stmt.setInt(2, i);
                    stmt.executeUpdate();
                    try (ResultSet rs = stmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            dayIds.add(rs.getInt(1));
                        }
                    }
                }
            }
        } catch (Exception e) {
            Logger.getLogger(ProgramWeekDAO.class.getName()).log(Level.SEVERE, "Add days for week failed", e);
        }
        return dayIds;
    }

    public static boolean deleteProgramWeek(int weekId) {
        String deleteDaysSql = "DELETE FROM ProgramDay WHERE WeekID = ?";
        String deleteWeekSql = "DELETE FROM ProgramWeek WHERE WeekID = ?";
        try (Connection con = ConnectDatabase.getInstance().openConnection();
             PreparedStatement stmtDays = con.prepareStatement(deleteDaysSql);
             PreparedStatement stmtWeek = con.prepareStatement(deleteWeekSql)) {

            stmtDays.setInt(1, weekId);
            stmtDays.executeUpdate();

            stmtWeek.setInt(1, weekId);
            int affected = stmtWeek.executeUpdate();
            return affected > 0;
        } catch (Exception e) {
            Logger.getLogger(ProgramWeekDAO.class.getName()).log(Level.SEVERE, "Delete program week failed", e);
        }
        return false;
    }

    public List<ProgramWeek> getWeeksByProgramId(int programId) {
        List<ProgramWeek> list = new ArrayList<>();
        String sql = "SELECT * FROM ProgramWeek WHERE ProgramID = ?";
        try (Connection con = ConnectDatabase.getInstance().openConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, programId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ProgramWeek w = new ProgramWeek();
                    w.setWeekId(rs.getInt("WeekID"));
                    w.setProgramId(rs.getInt("ProgramID"));
                    w.setWeekNumber(rs.getInt("WeekNumber"));
                    list.add(w);
                }
            }
        } catch (Exception e) {
            Logger.getLogger(ProgramWeekDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return list;
    }

    // Lấy tuần cụ thể theo programId và weekNumber
    public ProgramWeek getByProgramAndWeekNumber(int programId, int weekNumber) {
        String sql = "SELECT * FROM ProgramWeek WHERE ProgramID = ? AND WeekNumber = ?";
        try (Connection con = ConnectDatabase.getInstance().openConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, programId);
            stmt.setInt(2, weekNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    ProgramWeek w = new ProgramWeek();
                    w.setWeekId(rs.getInt("WeekID"));
                    w.setProgramId(rs.getInt("ProgramID"));
                    w.setWeekNumber(rs.getInt("WeekNumber"));
                    return w;
                }
            }
        } catch (Exception e) {
            Logger.getLogger(ProgramWeekDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    // Lấy tuần theo WeekID
    public ProgramWeek getWeekById(int weekId) {
        String sql = "SELECT * FROM ProgramWeek WHERE WeekID = ?";
        try (Connection con = ConnectDatabase.getInstance().openConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, weekId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    ProgramWeek w = new ProgramWeek();
                    w.setWeekId(rs.getInt("WeekID"));
                    w.setProgramId(rs.getInt("ProgramID"));
                    w.setWeekNumber(rs.getInt("WeekNumber"));
                    return w;
                }
            }
        } catch (Exception e) {
            Logger.getLogger(ProgramWeekDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    // Lấy tuần cao nhất của chương trình
    public int getMaxWeekNumber(int programId) {
        String sql = "SELECT MAX(WeekNumber) FROM ProgramWeek WHERE ProgramID = ?";
        try (Connection con = ConnectDatabase.getInstance().openConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, programId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (Exception e) {
            Logger.getLogger(ProgramWeekDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return 0;
    }
}
