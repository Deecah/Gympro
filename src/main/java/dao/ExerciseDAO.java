package dao;

import connectDB.ConnectDatabase;
import model.Exercise;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExerciseDAO {

    public int addExerciseToWorkout(int workoutId, int exerciseId, int sets, int reps, int restTime, String notes) {
        String sql = "INSERT INTO Exercise (WorkoutID, ExerciseID, Sets, Reps, RestTimeSeconds, Notes) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = ConnectDatabase.getInstance().openConnection();
            ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, workoutId);
            ps.setInt(2, exerciseId);
            ps.setInt(3, sets);
            ps.setInt(4, reps);
            ps.setInt(5, restTime);
            ps.setString(6, notes);
            ps.executeUpdate();

            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            Logger.getLogger(ExerciseDAO.class.getName()).log(Level.SEVERE, null, e);
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
                Logger.getLogger(ExerciseDAO.class.getName()).log(Level.SEVERE, null, e);
            }
        }

        return -1;
    }

    public List<Exercise> getExercisesByWorkout(int workoutId) {
        String sql = "SELECT e.*, el.Name AS ExerciseName, el.VideoURL, el.Description "
                + "FROM Exercise e "
                + "JOIN ExerciseLibrary el ON e.ExerciseID = el.ExerciseID "
                + "WHERE e.WorkoutID = ?";
        List<Exercise> list = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = ConnectDatabase.getInstance().openConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, workoutId);
            rs = ps.executeQuery();

            while (rs.next()) {
                Exercise ex = new Exercise();
                ex.setWorkoutID(rs.getInt("WorkoutID"));
                ex.setExerciseId(rs.getInt("ExerciseID"));
                ex.setSets(rs.getInt("Sets"));
                ex.setReps(rs.getInt("Reps"));
                ex.setRestTimeSeconds(rs.getInt("RestTimeSeconds"));
                ex.setNotes(rs.getString("Notes"));
                ex.setExerciseName(rs.getString("ExerciseName"));
                ex.setVideoURL(rs.getString("VideoURL"));
                ex.setDescription(rs.getString("Description"));
                list.add(ex);
            }
        } catch (Exception e) {
            Logger.getLogger(ExerciseDAO.class.getName()).log(Level.SEVERE, null, e);
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
                Logger.getLogger(ExerciseDAO.class.getName()).log(Level.SEVERE, null, e);
            }
        }

        return list;
    }

    public boolean deleteExerciseFromWorkout(int id) {
        String sql = "DELETE FROM WorkoutExercise WHERE Id = ?";
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = ConnectDatabase.getInstance().openConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            Logger.getLogger(ExerciseDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                Logger.getLogger(ExerciseDAO.class.getName()).log(Level.SEVERE, null, e);
            }
        }

        return false;
    }

    public boolean updateExerciseInWorkout(int id, int sets, int reps, int restTime, String notes) {
        String sql = "UPDATE WorkoutExercise SET Sets = ?, Reps = ?, RestTimeSeconds = ?, Notes = ? WHERE Id = ?";
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = ConnectDatabase.getInstance().openConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, sets);
            ps.setInt(2, reps);
            ps.setInt(3, restTime);
            ps.setString(4, notes);
            ps.setInt(5, id);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            Logger.getLogger(ExerciseDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                Logger.getLogger(ExerciseDAO.class.getName()).log(Level.SEVERE, null, e);
            }
        }

        return false;
    }

}
