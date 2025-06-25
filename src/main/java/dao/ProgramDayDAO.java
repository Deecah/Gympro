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
        ConnectDatabase db = ConnectDatabase.getInstance();
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            con = db.openConnection();
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
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (con != null) con.close();
            } catch (SQLException ex) {
                Logger.getLogger(ProgramDayDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return list;
    }
    
    
}
