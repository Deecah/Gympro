// File: dao/ProgramDAO.java
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
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            con = ConnectDatabase.getInstance().openConnection();
            stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, programId);
            stmt.setInt(2, weekNumber);
            stmt.executeUpdate();
            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            Logger.getLogger(ProgramWeekDAO.class.getName()).log(Level.SEVERE, "Add program week failed", e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                Logger.getLogger(ProgramWeekDAO.class.getName()).log(Level.SEVERE, "Close resources failed", e);
            }
        }
        return -1;
    }

    public static ArrayList<Integer> addDaysForWeek(int weekId) {
        String sql = "INSERT INTO ProgramDay (WeekID, DayNumber) VALUES (?, ?)";
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<Integer> dayIds = new ArrayList<>();

        try {
            con = ConnectDatabase.getInstance().openConnection();
            for (int i = 1; i <= 7; i++) {
                stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                stmt.setInt(1, weekId);
                stmt.setInt(2, i);
                stmt.executeUpdate();
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    dayIds.add(rs.getInt(1));
                }
                stmt.close();
            }
        } catch (Exception e) {
            Logger.getLogger(ProgramWeekDAO.class.getName()).log(Level.SEVERE, "Add days for week failed", e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                Logger.getLogger(ProgramWeekDAO.class.getName()).log(Level.SEVERE, "Close resources failed", e);
            }
        }
        return dayIds;
    }

    public static boolean deleteProgramWeek(int weekId) {
        String deleteDaysSql = "DELETE FROM ProgramDay WHERE WeekID = ?";
        String deleteWeekSql = "DELETE FROM ProgramWeek WHERE WeekID = ?";
        Connection con = null;
        PreparedStatement stmtDays = null;
        PreparedStatement stmtWeek = null;

        try {
            con = ConnectDatabase.getInstance().openConnection();
            stmtDays = con.prepareStatement(deleteDaysSql);
            stmtDays.setInt(1, weekId);
            stmtDays.executeUpdate();

            stmtWeek = con.prepareStatement(deleteWeekSql);
            stmtWeek.setInt(1, weekId);
            int affected = stmtWeek.executeUpdate();
            return affected > 0;
        } catch (Exception e) {
            Logger.getLogger(ProgramWeekDAO.class.getName()).log(Level.SEVERE, "Delete program week failed", e);
        } finally {
            try {
                if (stmtDays != null) stmtDays.close();
                if (stmtWeek != null) stmtWeek.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                Logger.getLogger(ProgramWeekDAO.class.getName()).log(Level.SEVERE, "Close resources failed", e);
            }
        }
        return false;
    }
    
     public List<ProgramWeek> getWeeksByProgramId(int programId) {
        String sql = "SELECT * FROM ProgramWeek WHERE ProgramID = ?";
        List<ProgramWeek> list = new ArrayList<>();
        ConnectDatabase db = ConnectDatabase.getInstance();
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            con = db.openConnection();
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, programId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                ProgramWeek w = new ProgramWeek();
                w.setWeekId(rs.getInt("WeekID"));
                w.setProgramId(rs.getInt("ProgramID"));
                w.setWeekNumber(rs.getInt("WeekNumber"));
                list.add(w);
            }
        } catch (ClassNotFoundException | SQLException e) {
            Logger.getLogger(ProgramWeekDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (con != null) con.close();
            } catch (SQLException ex) {
                Logger.getLogger(ProgramWeekDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return list;
    }
}