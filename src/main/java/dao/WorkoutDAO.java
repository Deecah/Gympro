
package dao;

import connectDB.ConnectDatabase;
import model.Workout;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WorkoutDAO {

    public int createWorkout(int dayId, String title, String notes, LocalTime startTime, LocalTime endTime) {
        String sql = "INSERT INTO Workout (DayID, Title, Notes, StartTime, EndTime) VALUES (?, ?, ?, ?, ?)";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = ConnectDatabase.getInstance().openConnection();
            ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, dayId);
            ps.setString(2, title);
            ps.setString(3, notes);
            ps.setTime(4, startTime != null ? Time.valueOf(startTime) : null);
            ps.setTime(5, endTime != null ? Time.valueOf(endTime) : null);
            ps.executeUpdate();

            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // WorkoutID
            }
        } catch (Exception e) {
            Logger.getLogger(WorkoutDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                Logger.getLogger(WorkoutDAO.class.getName()).log(Level.SEVERE, null, e);
            }
        }

        return -1;
    }

    public List<Workout> getWorkoutsByDayId(int dayId) {
        String sql = "SELECT * FROM Workout WHERE DayID = ?";
        List<Workout> list = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = ConnectDatabase.getInstance().openConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, dayId);
            rs = ps.executeQuery();

            while (rs.next()) {
                Workout w = new Workout();
                w.setWorkoutID(rs.getInt("WorkoutID"));
                w.setDayID(rs.getInt("DayID"));
                w.setTitle(rs.getString("Title"));
                w.setNotes(rs.getString("Notes"));
                w.setCreatedAt(rs.getTimestamp("CreatedAt"));
                Time start = rs.getTime("StartTime");
                Time end = rs.getTime("EndTime");
                w.setStartTime(start != null ? start.toLocalTime() : null);
                w.setEndTime(end != null ? end.toLocalTime() : null);
                list.add(w);
            }
        } catch (Exception e) {
            Logger.getLogger(WorkoutDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                Logger.getLogger(WorkoutDAO.class.getName()).log(Level.SEVERE, null, e);
            }
        }

        return list;
    }

    public int getDayIdByWorkoutId(int workoutId) {
        String sql = "SELECT DayID FROM Workout WHERE WorkoutID = ?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = ConnectDatabase.getInstance().openConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, workoutId);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("DayID");
            }
        } catch (Exception e) {
            Logger.getLogger(WorkoutDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                Logger.getLogger(WorkoutDAO.class.getName()).log(Level.SEVERE, null, e);
            }
        }

        return -1;
    }
    
    public Map<LocalDate, List<Workout>> getScheduleMap(int customerId) {
        Map<LocalDate, List<Workout>> map = new TreeMap<>();

        String sql = "SELECT " +
                "DATEADD(DAY, ((pw.WeekNumber - 1) * 7 + (pd.DayNumber - 1)), cp.StartDate) AS WorkoutDate, " +
                "w.WorkoutID, w.DayID, w.Title, w.Notes, w.StartTime, w.EndTime, w.CreatedAt " +
                "FROM CustomerProgram cp " +
                "JOIN Program p ON cp.ProgramID = p.ProgramID " +
                "JOIN ProgramWeek pw ON p.ProgramID = pw.ProgramID " +
                "JOIN ProgramDay pd ON pw.WeekID = pd.WeekID " +
                "JOIN Workout w ON pd.DayID = w.DayID " +
                "WHERE cp.CustomerID = ?";

        try (Connection con = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                LocalDate date = rs.getDate("WorkoutDate").toLocalDate();

                Workout w = new Workout();
                w.setWorkoutID(rs.getInt("WorkoutID"));
                w.setDayID(rs.getInt("DayID"));
                w.setTitle(rs.getString("Title"));
                w.setNotes(rs.getString("Notes"));
                w.setStartTime(rs.getTime("StartTime").toLocalTime());
                w.setEndTime(rs.getTime("EndTime").toLocalTime());
                w.setCreatedAt(rs.getTimestamp("CreatedAt"));

                map.computeIfAbsent(date, k -> new ArrayList<>()).add(w);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }

    public List<LocalDate> getCurrentWeekDates() {
        LocalDate today = LocalDate.now();
        LocalDate monday = today.minusDays(today.getDayOfWeek().getValue() - 1);
        List<LocalDate> week = new ArrayList<>();
        for (int i = 0; i < 7; i++) week.add(monday.plusDays(i));
        return week;
    }
}
