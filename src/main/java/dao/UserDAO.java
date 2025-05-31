/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import connectDB.ConnectDatabase;
import model.User;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.GoogleAccount;
import Utils.PasswordUtil;

public class UserDAO {

    public boolean addUser(User user) {
        if (isEmailExists(user.getEmail())) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.WARNING, "Email already exists!");
            return false;
        }

        String sql = "INSERT INTO Users (Name, Email, Password, Role) VALUES (?, ?, ?, ?)";
        String insertRoleSQL = "";
        ConnectDatabase db = ConnectDatabase.getInstance();
        Connection con = null;
        PreparedStatement userStmt = null;
        PreparedStatement roleStmt = null;
        ResultSet generatedKeys = null;

        try {
            con = db.openConnection();
            con.setAutoCommit(false); // atomic transaction
            // Insert user vào bảng Users
            userStmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            userStmt.setString(1, user.getUserName());
            userStmt.setString(2, user.getEmail());
            userStmt.setBytes(3, user.getPassword());
            userStmt.setString(4, user.getRole());

            int affectedRows = userStmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Inserting user failed, no rows affected.");
            }

            generatedKeys = userStmt.getGeneratedKeys();
            if (!generatedKeys.next()) {
                throw new SQLException("Inserting user failed, no ID obtained.");
            }
            int userId = generatedKeys.getInt(1);

            // Insert vào bảng Customer hoặc Trainer
            switch (user.getRole()) {
                case "Customer":
                    insertRoleSQL = "INSERT INTO Customer (Id) VALUES (?)";
                    break;
                case "Trainer":
                    insertRoleSQL = "INSERT INTO Trainer (Id) VALUES (?)";
                    break;
                default:
                    throw new SQLException("Unsupported role: " + user.getRole());
            }

            roleStmt = con.prepareStatement(insertRoleSQL);
            roleStmt.setInt(1, userId);
            roleStmt.executeUpdate();

            con.commit();
            return true;

        } catch (ClassNotFoundException | SQLException e) {
            try {
                if (con != null) {
                    con.rollback(); // rollback nếu lỗi
                }
            } catch (SQLException ex) {
                Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, "Rollback failed", ex);
            }
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, "Insert user failed", e);
            return false;

        } finally {
            try {
                if (generatedKeys != null) {
                    generatedKeys.close();
                }
                if (roleStmt != null) {
                    roleStmt.close();
                }
                if (userStmt != null) {
                    userStmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, "Close resources failed", ex);
            }
        }
    }
    
    public boolean addUserFromGoogle(GoogleAccount googleAcc) {
        User user = new User();
        user.setUserName(googleAcc.getName());
        user.setEmail(googleAcc.getEmail());
        user.setPassword(PasswordUtil.generateRandomPassword()); 
        user.setRole("Customer"); 
        return addUser(user); 
    }

    
    public boolean isEmailExists(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        ConnectDatabase db = ConnectDatabase.getInstance();
        Connection con = null;
        PreparedStatement statement = null;
        try {
            con = db.openConnection();
            statement = con.prepareStatement(sql); 
            statement.setString(1, email);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (ClassNotFoundException e) {
            Logger.getLogger(ConnectDatabase.class.getName()).log(Level.SEVERE, null, e);
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                statement.close();
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }
  
    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM Users WHERE Email = ?";
        ConnectDatabase db = ConnectDatabase.getInstance();
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            con = db.openConnection();
            stmt = con.prepareStatement(sql);
            stmt.setString(1, email);
            rs = stmt.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("Id"));
                user.setUserName(rs.getString("Name"));
                user.setEmail(rs.getString("Email"));
                user.setPassword(rs.getBytes("Password"));
                user.setRole(rs.getString("Role"));
                user.setStatus(rs.getString("Status"));
                return user;
            }

        } catch (ClassNotFoundException | SQLException e) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    

}
