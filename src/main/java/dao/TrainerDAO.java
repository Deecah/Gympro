
package dao;

import connectDB.ConnectDatabase;
import model.Trainer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TrainerDAO {

    public Trainer getProfile(int userId) {
        String userSQL = "SELECT Name, Gender, Email, Phone, Address, AvatarUrl FROM Users WHERE Id = ?";
        String trainerSQL = "SELECT ExperienceYears, Description, Specialization FROM Trainer WHERE Id = ?";

        ConnectDatabase db = ConnectDatabase.getInstance();
        Connection con = null;
        PreparedStatement userStmt = null;
        PreparedStatement trainerStmt = null;
        ResultSet userRs = null;
        ResultSet trainerRs = null;

        try {
            con = db.openConnection();

            userStmt = con.prepareStatement(userSQL);
            userStmt.setInt(1, userId);
            userRs = userStmt.executeQuery();

            if (!userRs.next()) {
                return null;  
            }

            Trainer trainer = new Trainer();
            trainer.setUserId(userId);
            trainer.setUserName(userRs.getString("Name"));
            trainer.setGender(userRs.getString("Gender"));
            trainer.setEmail(userRs.getString("Email"));
            trainer.setPhone(userRs.getString("Phone"));
            trainer.setAddress(userRs.getString("Address"));
            trainer.setAvatarUrl(userRs.getString("AvatarUrl"));

            trainerStmt = con.prepareStatement(trainerSQL);
            trainerStmt.setInt(1, userId);
            trainerRs = trainerStmt.executeQuery();

            if (trainerRs.next()) {
                trainer.setExperienceYears(trainerRs.getInt("ExperienceYears"));
                trainer.setDescription(trainerRs.getString("Description"));
                trainer.setSpecialization(trainerRs.getString("Specialization"));
            }

            return trainer;

        } catch (Exception e) {
            Logger.getLogger(TrainerDAO.class.getName()).log(Level.SEVERE, "Get profile failed", e);
            return null;

        } finally {
            try {
                if (userRs != null) {
                    userRs.close();
                }
                if (trainerRs != null) {
                    trainerRs.close();
                }
                if (userStmt != null) {
                    userStmt.close();
                }
                if (trainerStmt != null) {
                    trainerStmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(TrainerDAO.class.getName()).log(Level.SEVERE, "Close resources failed", ex);
            }
        }
    }

    public boolean editProfile(Trainer trainer) {
        String updateUserSQL = "UPDATE Users SET Name = ?, Gender = ?, Phone = ?, Address = ?, AvatarUrl = ? WHERE Id = ?";
        String updateTrainerSQL = "UPDATE Trainer SET ExperienceYears = ?, Description = ?, Specialization = ? WHERE Id = ?";

        ConnectDatabase db = ConnectDatabase.getInstance();
        Connection con = null;
        PreparedStatement userStmt = null;
        PreparedStatement trainerStmt = null;

        try {
            con = db.openConnection();
            con.setAutoCommit(false);

            // Update bảng Users
            userStmt = con.prepareStatement(updateUserSQL);
            userStmt.setString(1, trainer.getUserName());
            userStmt.setString(2, trainer.getGender());
            userStmt.setString(3, trainer.getPhone());
            userStmt.setString(4, trainer.getAddress());
            userStmt.setString(5, trainer.getAvatarUrl());
            userStmt.setInt(6, trainer.getUserId());
            userStmt.executeUpdate();

            // Update bảng Trainer
            trainerStmt = con.prepareStatement(updateTrainerSQL);
            trainerStmt.setInt(1, trainer.getExperienceYears());
            trainerStmt.setString(2, trainer.getDescription());
            trainerStmt.setString(3, trainer.getSpecialization());
            trainerStmt.setInt(4, trainer.getUserId());
            trainerStmt.executeUpdate();

            con.commit();
            return true;

        } catch (Exception e) {
            try {
                if (con != null) {
                    con.rollback();
                }
            } catch (SQLException ex) {
                Logger.getLogger(TrainerDAO.class.getName()).log(Level.SEVERE, "Rollback failed", ex);
            }
            Logger.getLogger(TrainerDAO.class.getName()).log(Level.SEVERE, "Edit profile failed", e);
            return false;

        } finally {
            try {
                if (userStmt != null) {
                    userStmt.close();
                }
                if (trainerStmt != null) {
                    trainerStmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(TrainerDAO.class.getName()).log(Level.SEVERE, "Close resources failed", ex);
            }
        }
    }
    public List<Trainer> searchByKeyword(String keyword) {
        List<Trainer> list = new ArrayList<>();
        String sql = "SELECT t.*, u.Name, u.Email " +
             "FROM Trainer t " +
             "JOIN Users u ON t.Id = u.Id " +
             "WHERE u.Name LIKE ? OR t.Specialization LIKE ?";

        try (Connection con = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Trainer t = new Trainer();
                t.setUserId(rs.getInt("Id"));
                t.setUserName(rs.getString("Name"));
                t.setEmail(rs.getString("Email"));
                t.setSpecialization(rs.getString("Specialization"));
                t.setDescription(rs.getString("Description"));
                t.setExperienceYears(rs.getInt("ExperienceYears"));
                list.add(t);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    
    
}