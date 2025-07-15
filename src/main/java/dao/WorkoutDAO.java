package dao;

import connectDB.ConnectDatabase;
import model.Workout;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WorkoutDAO {

    public int createWorkout(int dayId, String title, String rounds, String notes) {
        String sql = "INSERT INTO Workout (DayID, Title, Rounds, Notes) VALUES (?, ?, ?, ?)";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = ConnectDatabase.getInstance().openConnection();
            ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, dayId);
            ps.setString(2, title);
            ps.setString(3, rounds);
            ps.setString(4, notes);
            ps.executeUpdate();

            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // WorkoutID
            }
        } catch (Exception e) {
            Logger.getLogger(WorkoutDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
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
                w.setRounds(rs.getString("Rounds"));
                w.setNotes(rs.getString("Notes"));
                w.setCreatedAt(rs.getTimestamp("CreatedAt"));
                list.add(w);
            }
        } catch (Exception e) {
            Logger.getLogger(WorkoutDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
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
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                Logger.getLogger(WorkoutDAO.class.getName()).log(Level.SEVERE, null, e);
            }
        }

        return -1;
    }
}
