package dao;

import connectDB.ConnectDatabase;
import model.ExerciseLibrary;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExerciseLibraryDAO {

    public List<ExerciseLibrary> getAllExercises() {
        String sql = "SELECT * FROM ExerciseLibrary";
        List<ExerciseLibrary> list = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = ConnectDatabase.getInstance().openConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                ExerciseLibrary e = new ExerciseLibrary();
                e.setExerciseID(rs.getInt("ExerciseID"));
                e.setName(rs.getString("Name"));
                e.setDescription(rs.getString("Description"));
                e.setVideoURL(rs.getString("VideoURL"));
                e.setMuscleGroup(rs.getString("MuscleGroup"));
                e.setEquipment(rs.getString("Equipment"));
                list.add(e);
            }
        } catch (Exception e) {
            Logger.getLogger(ExerciseLibraryDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                Logger.getLogger(ExerciseLibraryDAO.class.getName()).log(Level.SEVERE, null, e);
            }
        }

        return list;
    }
}
