
package dao;

import connectDB.ConnectDatabase;
import model.Trainer;
import java.sql.*;
import java.util.ArrayList;
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
    public ArrayList<Trainer> getAllTrainers() {
        ArrayList<Trainer> trainerList = new ArrayList<>();
        String sql = "SELECT U.Id, U.Name, U.Gender, U.Email, U.Phone, U.Address, U.AvatarUrl, " +
                     "T.ExperienceYears, T.Description, T.Specialization " +
                     "FROM Users U " +
                     "INNER JOIN Trainer T ON U.Id = T.Id " +
                     "WHERE U.Role = 'trainer'";

        ConnectDatabase db = ConnectDatabase.getInstance();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = db.openConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Trainer trainer = new Trainer();
                trainer.setUserId(rs.getInt("Id"));
                trainer.setUserName(rs.getString("Name"));
                trainer.setGender(rs.getString("Gender"));
                trainer.setEmail(rs.getString("Email"));
                trainer.setPhone(rs.getString("Phone"));
                trainer.setAddress(rs.getString("Address"));
                trainer.setAvatarUrl(rs.getString("AvatarUrl"));

                // Thông tin đặc trưng của Trainer
                trainer.setExperienceYears(rs.getInt("ExperienceYears"));
                trainer.setDescription(rs.getString("Description"));
                trainer.setSpecialization(rs.getString("Specialization"));

                // Lưu ý: Các thuộc tính Role và Status của User sẽ không được set nếu User không phải là Trainer
                // Nếu bạn muốn lấy cả Role và Status của Users cho Trainer, bạn cần thêm chúng vào SELECT và set.
                // Ví dụ: trainer.setRole(rs.getString("Role")); trainer.setStatus(rs.getString("Status"));

                trainerList.add(trainer);
            }

        } catch (Exception e) {
            Logger.getLogger(TrainerDAO.class.getName()).log(Level.SEVERE, "Get all trainers failed", e);
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
            } catch (SQLException ex) {
                Logger.getLogger(TrainerDAO.class.getName()).log(Level.SEVERE, "Close resources failed", ex);
            }
        }
        return trainerList;
    }
}
