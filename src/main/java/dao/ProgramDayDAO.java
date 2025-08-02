package dao;

import connectDB.ConnectDatabase;
import model.ProgramDay;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProgramDayDAO {

    // Lấy DayID theo ProgramID và ngày (yyyy-MM-dd), nếu chưa có thì tạo mới
    public int getOrCreateDayId(int programId, String dateStr) {
        int weekId = getOrCreateWeekId(programId, dateStr);
        int dayNumber = getDayNumberFromDate(dateStr); // Tùy logic, ví dụ: 1=Monday, 2=Tuesday,...
        int dayId = -1;

        String selectDay = "SELECT DayID FROM ProgramDay WHERE WeekID=? AND DayNumber=?";
        try (Connection con = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = con.prepareStatement(selectDay)) {
            ps.setInt(1, weekId);
            ps.setInt(2, dayNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    dayId = rs.getInt("DayID");
                }
            }
        } catch (Exception e) { e.printStackTrace(); }

        if (dayId != -1) return dayId;

        // Nếu chưa có thì tạo mới
        String insertDay = "INSERT INTO ProgramDay (WeekID, DayNumber) VALUES (?, ?)";
        try (Connection con = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = con.prepareStatement(insertDay, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, weekId);
            ps.setInt(2, dayNumber);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    dayId = rs.getInt(1);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }

        return dayId;
    }

    // Lấy WeekID theo ProgramID và ngày, nếu chưa có thì tạo mới
    public int getOrCreateWeekId(int programId, String dateStr) {
        int weekNumber = getWeekNumberFromDate(dateStr);
        int weekId = -1;

        String selectWeek = "SELECT WeekID FROM ProgramWeek WHERE ProgramID=? AND WeekNumber=?";
        try (Connection con = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = con.prepareStatement(selectWeek)) {
            ps.setInt(1, programId);
            ps.setInt(2, weekNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    weekId = rs.getInt("WeekID");
                }
            }
        } catch (Exception e) { e.printStackTrace(); }

        if (weekId != -1) return weekId;

        // Nếu chưa có thì tạo mới
        String insertWeek = "INSERT INTO ProgramWeek (ProgramID, WeekNumber) VALUES (?, ?)";
        try (Connection con = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = con.prepareStatement(insertWeek, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, programId);
            ps.setInt(2, weekNumber);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    weekId = rs.getInt(1);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }

        return weekId;
    }

    // Chuyển yyyy-MM-dd thành số tuần trong năm (ISO)
    private int getWeekNumberFromDate(String dateStr) {
        java.time.LocalDate date = java.time.LocalDate.parse(dateStr);
        java.time.temporal.WeekFields weekFields = java.time.temporal.WeekFields.ISO;
        return date.get(weekFields.weekOfWeekBasedYear());
    }

    // Chuyển yyyy-MM-dd thành số thứ tự trong tuần (1=Monday, 7=Sunday)
    private int getDayNumberFromDate(String dateStr) {
        java.time.LocalDate date = java.time.LocalDate.parse(dateStr);
        return date.getDayOfWeek().getValue();
    }

    public List<ProgramDay> getDaysByWeekId(int weekId) {
        List<ProgramDay> days = new ArrayList<>();
        String sql = "SELECT DayID, WeekID, DayNumber FROM ProgramDay WHERE WeekID = ? ORDER BY DayNumber ASC";
        try (Connection con = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, weekId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ProgramDay day = new ProgramDay();
                    day.setDayId(rs.getInt("DayID"));
                    day.setWeekId(rs.getInt("WeekID"));
                    day.setDayNumber(rs.getInt("DayNumber"));
                    days.add(day);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return days;
    }

    // New method to get days for a program, grouped by WeekID
    public Map<Integer, List<ProgramDay>> get(int programId) {
        Map<Integer, List<ProgramDay>> daysMap = new HashMap<>();
        String sql = "SELECT pd.DayID, pd.WeekID, pd.DayNumber " +
                "FROM ProgramDay pd " +
                "JOIN ProgramWeek pw ON pd.WeekID = pw.WeekID " +
                "WHERE pw.ProgramID = ? " +
                "ORDER BY pd.WeekID, pd.DayNumber ASC";
        try (Connection con = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, programId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int weekId = rs.getInt("WeekID");
                    ProgramDay day = new ProgramDay();
                    day.setDayId(rs.getInt("DayID"));
                    day.setWeekId(weekId);
                    day.setDayNumber(rs.getInt("DayNumber"));
                    daysMap.computeIfAbsent(weekId, k -> new ArrayList<>()).add(day);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return daysMap;
    }
}