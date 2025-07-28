package dao;

import connectDB.ConnectDatabase;
import model.ProgramDay;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProgramDayDAO {

    public List<ProgramDay> getDaysByWeekId(int weekId) {
        String sql = "SELECT * FROM ProgramDay WHERE WeekID = ?";
        List<ProgramDay> list = new ArrayList<>();
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            con = ConnectDatabase.getInstance().openConnection();
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, weekId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                ProgramDay d = new ProgramDay();
                d.setDayId(rs.getInt("DayID"));
                d.setWeekId(rs.getInt("WeekID"));
                d.setDayNumber(rs.getInt("DayNumber"));
                list.add(d);
            }
        } catch (ClassNotFoundException | SQLException e) {
            Logger.getLogger(ProgramDayDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            closeResources(rs, stmt, con);
        }
        return list;
    }

    public int getProgramIdByDayId(int dayId) {
        String sql = "SELECT pw.ProgramID "
                   + "FROM ProgramDay pd "
                   + "JOIN ProgramWeek pw ON pd.WeekID = pw.WeekID "
                   + "WHERE pd.DayID = ?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = ConnectDatabase.getInstance().openConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, dayId);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("ProgramID");
            }
        } catch (Exception e) {
            Logger.getLogger(ProgramDayDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            closeResources(rs, ps, con);
        }
        return -1;
    }

    // lấy 1 ngày cụ thể theo ID
    public ProgramDay getDayById(int dayId) {
        String sql = "SELECT * FROM ProgramDay WHERE DayID = ?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = ConnectDatabase.getInstance().openConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, dayId);
            rs = ps.executeQuery();
            if (rs.next()) {
                ProgramDay d = new ProgramDay();
                d.setDayId(rs.getInt("DayID"));
                d.setWeekId(rs.getInt("WeekID"));
                d.setDayNumber(rs.getInt("DayNumber"));
                return d;
            }
        } catch (Exception e) {
            Logger.getLogger(ProgramDayDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            closeResources(rs, ps, con);
        }
        return null;
    }

    // lấy ngày theo WeekID + DayNumber
    public ProgramDay getDayByWeekIdAndDayNumber(int weekId, int dayNumber) {
        String sql = "SELECT * FROM ProgramDay WHERE WeekID = ? AND DayNumber = ?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = ConnectDatabase.getInstance().openConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, weekId);
            ps.setInt(2, dayNumber);
            rs = ps.executeQuery();
            if (rs.next()) {
                ProgramDay d = new ProgramDay();
                d.setDayId(rs.getInt("DayID"));
                d.setWeekId(rs.getInt("WeekID"));
                d.setDayNumber(rs.getInt("DayNumber"));
                return d;
            }
        } catch (Exception e) {
            Logger.getLogger(ProgramDayDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            closeResources(rs, ps, con);
        }
        return null;
    }

    private void closeResources(ResultSet rs, Statement stmt, Connection con) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (con != null) con.close();
        } catch (SQLException e) {
            Logger.getLogger(ProgramDayDAO.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
