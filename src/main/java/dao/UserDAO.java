package dao;

import connectDB.ConnectDatabase;
import model.User;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.GoogleAccount;
import Utils.PasswordUtil;


public class UserDAO {

    public boolean updateAvatar(int userId, String avatarUrl) {
        try (Connection con = ConnectDatabase.getInstance().openConnection()) {
            String sql = "UPDATE Users SET Avatar_Url = ? WHERE Id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, avatarUrl);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public User getUserByEmail(String email) {
        try (Connection conn = ConnectDatabase.getInstance().openConnection()) {
            String sql = "SELECT * FROM Users WHERE Email = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("Id"));
                user.setUserName(rs.getString("Name"));
                user.setGender(rs.getString("Gender"));
                user.setEmail(rs.getString("Email"));
                user.setPhone(rs.getString("Phone"));
                user.setAddress(rs.getString("Address"));
                user.setAvatarUrl(rs.getString("Avatar_Url"));
                user.setPassword(rs.getBytes("Password"));
                user.setRole(rs.getString("Role"));
                user.setStatus(rs.getString("Status"));
                return user;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public User getUserById(int userId) {
        User user = null;
        try (Connection con = ConnectDatabase.getInstance().openConnection()) {
            String sql = "SELECT * FROM Users WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUserName(rs.getString("name"));
                user.setGender(rs.getString("gender"));
                user.setEmail(rs.getString("email"));
                user.setPhone(rs.getString("phone"));
                user.setAddress(rs.getString("address"));
                user.setAvatarUrl(rs.getString("avatar_url"));
                user.setPassword(rs.getBytes("password"));
                user.setRole(rs.getString("role"));
                user.setStatus(rs.getString("status"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public User getUserByEmailAndPassword(String email, String hashedPassword) throws ClassNotFoundException, SQLException {
        try (Connection conn = ConnectDatabase.getInstance().openConnection()) {
            String sql = "SELECT * FROM Users WHERE Email = ? AND Password = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, hashedPassword);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("Id"));
                user.setUserName(rs.getString("Name"));
                user.setGender(rs.getString("Gender"));
                user.setEmail(rs.getString("Email"));
                user.setPhone(rs.getString("Phone"));
                user.setAddress(rs.getString("Address"));
                user.setAvatarUrl(rs.getString("Avatar_Url"));
                user.setPassword(rs.getBytes("Password"));
                return user;

            }
        }
        return null;
    }





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
  
   

    public boolean updateUser(User user) {
        try (Connection con = ConnectDatabase.getInstance().openConnection()) {
            String sql = "UPDATE Users SET Name = ?, Gender = ?, Email = ?, Phone = ?, Address = ?, Role = ?, Status = ? WHERE Id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, user.getUserName());
            ps.setString(2, user.getGender());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPhone());
            ps.setString(5, user.getAddress());
            ps.setString(6, user.getRole());
            ps.setString(7, user.getStatus());
            ps.setInt(8, user.getId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
     public boolean updatePassword(int userId, byte[] newPassword) {
    try (Connection con = ConnectDatabase.getInstance().openConnection()) {
        String sql = "UPDATE Users SET Password = ? WHERE Id = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setBytes(1, newPassword);
        ps.setInt(2, userId);
        return ps.executeUpdate() > 0;
    } catch (Exception e) {
        e.printStackTrace();
    }
    return false;
}
    
}