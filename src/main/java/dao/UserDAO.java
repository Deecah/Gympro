
package dao;

import connectDB.ConnectDatabase;
import model.User;
import model.GoogleAccount;
import Utils.PasswordUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDAO {

    public ArrayList<User> getCustomersWithActiveContracts(int trainerId) {
        ArrayList<User> customers = new ArrayList<>();
        String sql = "SELECT u.* FROM Users u JOIN Contract c ON u.UserID = c.CustomerID " +
                "WHERE c.TrainerID = ? AND c.Status = 'active'";
        try (Connection conn = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, trainerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    User user = new User();
                    user.setUserId(rs.getInt("UserID"));
                    user.setUserName(rs.getString("UserName"));
                    user.setEmail(rs.getString("Email"));
                    user.setRole(rs.getString("Role"));
                    customers.add(user);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customers;
    }

    public ArrayList<User> getCustomersByTrainer(int trainerId) {
        ArrayList<User> customers = new ArrayList<>();
        String sql = "SELECT u.Id, u.Name, u.Email, u.Role " +
                     "FROM Customer c " +
                     "JOIN Users u ON c.Id = u.Id " +
                     "JOIN Contracts ct ON c.Id = ct.CustomerID " +
                     "WHERE ct.TrainerID = ?";
        try (Connection conn = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, trainerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    User user = new User();
                    user.setUserId(rs.getInt("Id"));
                    user.setUserName(rs.getString("Name"));
                    user.setEmail(rs.getString("Email"));
                    user.setRole(rs.getString("Role"));
                    customers.add(user);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customers;
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
                user.setUserId(rs.getInt("Id"));
                user.setUserName(rs.getString("Name"));
                user.setEmail(rs.getString("Email"));
                user.setPhone(rs.getString("Phone"));
                user.setAddress(rs.getString("Address"));
                user.setAvatarUrl(rs.getString("AvatarUrl"));
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

    public ArrayList<User> getAllUsers() {
        ArrayList<User> userList = new ArrayList<>();
        try (Connection conn = ConnectDatabase.getInstance().openConnection()) {
            String sql = "SELECT Id, Name, Gender, Email, Phone, Address, AvatarUrl, Role, Status FROM Users WHERE Role IN ('Trainer', 'Customer')";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("Id"));
                user.setUserName(rs.getString("Name"));
                user.setGender(rs.getString("Gender"));
                user.setEmail(rs.getString("Email"));
                user.setPhone(rs.getString("Phone"));
                user.setAddress(rs.getString("Address"));
                user.setAvatarUrl(rs.getString("AvatarUrl"));
                user.setRole(rs.getString("Role"));
                user.setStatus(rs.getString("Status"));
                userList.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userList;
    }

    public User getUserById(int userId) {
        User user = null;
        try (Connection con = ConnectDatabase.getInstance().openConnection()) {
            String sql = "SELECT Id, Name, Email, AvatarUrl FROM Users WHERE Id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                user = new User();
                user.setUserId(rs.getInt("Id"));
                user.setUserName(rs.getString("Name"));
                user.setEmail(rs.getString("Email"));
                user.setAvatarUrl(rs.getString("AvatarUrl"));
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
                user.setUserId(rs.getInt("Id"));
                user.setUserName(rs.getString("Name"));
                user.setGender(rs.getString("Gender"));
                user.setEmail(rs.getString("Email"));
                user.setPhone(rs.getString("Phone"));
                user.setAddress(rs.getString("Address"));
                user.setAvatarUrl(rs.getString("AvatarUrl"));
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
            con.setAutoCommit(false);
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
                    con.rollback();
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
        String sql = "SELECT COUNT(*) FROM Users WHERE Email = ?";
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
        } catch (ClassNotFoundException | SQLException e) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }

    public void deleteUser(String id) {
        String sql = "DELETE FROM Users WHERE Id = ?";
        ConnectDatabase db = ConnectDatabase.getInstance();
        Connection con = null;
        PreparedStatement statement = null;
        try {
            con = db.openConnection();
            statement = con.prepareStatement(sql);
            int userId = Integer.parseInt(id);
            statement.setInt(1, userId);
            statement.executeUpdate();
        } catch (ClassNotFoundException | SQLException e) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public boolean updateUser(User user) {
        try (Connection con = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = con.prepareStatement(
                     "UPDATE Users SET Name = ?, Gender = ?, Email = ?, Phone = ?, Address = ?, Role = ?, Status = ? WHERE Id = ?")) {
            ps.setString(1, user.getUserName());
            ps.setString(2, user.getGender());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPhone());
            ps.setString(5, user.getAddress());
            ps.setString(6, user.getRole());
            ps.setString(7, user.getStatus());
            ps.setInt(8, user.getUserId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
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

    public boolean updateAvatar(int userId, String avatarUrl) {
        String sql = "UPDATE Users SET AvatarUrl = ? WHERE Id = ?";
        ConnectDatabase db = ConnectDatabase.getInstance();
        Connection con = null;
        PreparedStatement statement = null;
        try {
            con = db.openConnection();
            statement = con.prepareStatement(sql);
            statement.setString(1, avatarUrl);
            statement.setInt(2, userId);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (ClassNotFoundException | SQLException e) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }

    public boolean banUser(int userId) {
        String sql = "UPDATE Users SET Status = ? WHERE Id = ?";
        ConnectDatabase db = ConnectDatabase.getInstance();
        Connection con = null;
        PreparedStatement statement = null;
        try {
            con = db.openConnection();
            statement = con.prepareStatement(sql);
            statement.setString(1, "Banned");
            statement.setInt(2, userId);
            statement.executeUpdate();
            return true;
        } catch (ClassNotFoundException | SQLException e) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, e);
            return false;
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public boolean unbanUser(int userId) {
        String sql = "UPDATE Users SET Status = ? WHERE Id = ?";
        ConnectDatabase db = ConnectDatabase.getInstance();
        Connection con = null;
        PreparedStatement statement = null;
        try {
            con = db.openConnection();
            statement = con.prepareStatement(sql);
            statement.setString(1, "Normal");
            statement.setInt(2, userId);
            statement.executeUpdate();
            return true;
        } catch (ClassNotFoundException | SQLException e) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, e);
            return false;
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public ArrayList<User> getUsersByRole(String role) {
        ArrayList<User> userList = new ArrayList<>();
        String sql = "SELECT Id, Name, Gender, Email, Phone, Address, AvatarUrl, Role, Status FROM Users WHERE Role = ?";
        try (Connection conn = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, role);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("Id"));
                user.setUserName(rs.getString("Name"));
                user.setGender(rs.getString("Gender"));
                user.setEmail(rs.getString("Email"));
                user.setPhone(rs.getString("Phone"));
                user.setAddress(rs.getString("Address"));
                user.setAvatarUrl(rs.getString("AvatarUrl"));
                user.setRole(rs.getString("Role"));
                user.setStatus(rs.getString("Status"));
                userList.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userList;
    }
}
