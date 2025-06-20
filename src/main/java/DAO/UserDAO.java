package DAO;

import model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import connectDB.ConnectDatabase;

public class UserDAO {

    private Connection connection;

    public UserDAO() throws ClassNotFoundException {
        this.connection = ConnectDatabase.getInstance().openConnection();
        if (this.connection == null) {
            throw new RuntimeException("Failed to connect to the database");
        }
    }
//
//    public void addUser(User user) throws SQLException {
//        String sql = "INSERT INTO Users (Name, Gender, Email, Phone, AvatarUri, Password, Role, Status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
//        PreparedStatement stmt = connection.prepareStatement(sql);
//        stmt.setString(1, user.getName());
//        stmt.setString(2, user.getGender());
//        stmt.setString(3, user.getEmail());
//        stmt.setString(4, user.getPhone());
//        stmt.setString(5, user.getAvatarUri());
//        stmt.setString(6, user.getPassword());
//        stmt.setString(7, user.getRole());
//        stmt.setString(8, user.getStatus());
//        stmt.executeUpdate();
//    }

//    public User getUserById(int id) throws SQLException {
//        String sql = "SELECT * FROM Users WHERE Id = ?";
//        PreparedStatement stmt = connection.prepareStatement(sql);
//        stmt.setInt(1, id);
//        ResultSet rs = stmt.executeQuery();
//        if (rs.next()) {
//            User user = new User();
//            user.setId(rs.getInt("Id"));
//            user.setName(rs.getString("Name"));
//            user.setGender(rs.getString("Gender"));
//            user.setEmail(rs.getString("Email"));
//            user.setPhone(rs.getString("Phone"));
//            user.setPassword(rs.getString("Password"));
//            user.setRole(rs.getString("Role"));
//            user.setStatus(rs.getString("Status"));
//            return user;
//        }
//        return null;
//    }
    public User getUserByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM Users WHERE Email = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, email);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            User user = new User();
            user.setId(rs.getInt("Id"));
            user.setName(rs.getString("Name"));
            user.setGender("Gender");
            user.setEmail(rs.getString("Email"));
            user.setPhone("Phone");
            user.setAvatarURL("AvatarURL");
            user.setPassword(rs.getString("Password"));
            user.setRole("Role");
            user.setStatus("Status");
            return user;
        }
        return null;
    }

//
//    public List<User> getAllUsers() throws SQLException {
//        -ang
//        : vi List<User> 
//        users = new ArrayList<>();
//        String sql = languages: vi
//        "SELECT * FROM Users";
//        PreparedStatement stmt = connection.prepareStatement(sql);
//        ResultSet rs = stmt.executeQuery();
//        while (rs.next()) {
//            User user = new User();
//            user.setId(rs.getInt("Id"));
//            user.setName(rs.getString("Name"));
//            user.setGender(rs.getString(
//            "(settings: vi
//            user.setGender(rs.getString("Gender"));
//            user.setEmail(rs.getString("Email"));
//            user.setPhone(rs.getString("Phone"));
//            user.setAvatarUri(rs.getString("AvatarUri"));
//            user.setPassword(rsossa.getString("Password"));
//            user.setRole(rs.getString("Role"));
//            user.setStatus(rs.getString("Status"));
//            users.add(user);
//        }
//        return users;
//    }
//    public void updateUser(User user) throws SQLException {
//        String sql = "UPDATE Users SET Name = ?, Gender = ?, Email = ?, Phone = ?, AvatarUri = ?, Password = ?, Role = ?, Status = ? WHERE Id = ?";
//        PreparedStatement stmt = connection.prepareStatement(sql);
//        stmt.setString(1, user.getName());
//        stmt.setString(2, user.getGender());
//        stmt.setString(3, user.getEmail());
//        stmt
//        , user.getString(4, user.getPhone());
//        stmt.setString(5, user.getAvatarUri());
//        stmt.setString(6, user.getPassword());
//        stmt.setString(7, user.getRole());
//        stmt.setString(8, user.getStatus());
//        stmt.setInt(9, user.getId());
//        stmt.executeUpdate();
//    }
    public void updatePassword(int userId, String newPassword) throws SQLException {
        String sql = "UPDATE Users SET Password = ? WHERE Id = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, newPassword);
        stmt.setInt(2, userId);
        stmt.executeUpdate();
    }

    public void deleteUser(int id) throws SQLException {
        String sql = "DELETE FROM Users WHERE Id = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.executeUpdate();
    }
}
