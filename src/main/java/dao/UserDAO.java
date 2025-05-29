package dao;

import connectDB.ConnectDatabase;
import model.User;

import java.sql.*;

public class UserDAO {

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
                user.setName(rs.getString("name"));
                user.setGender(rs.getString("gender"));
                user.setEmail(rs.getString("email"));
                user.setPhone(rs.getString("phone"));
                user.setAddress(rs.getString("address"));
                user.setAvatarUrl(rs.getString("avatar_url"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
                user.setStatus(rs.getString("status"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public User getUserByEmailAndPassword(String email, String hashedPassword) {
        try (Connection conn = ConnectDatabase.getInstance().openConnection()) {
            String sql = "SELECT * FROM Users WHERE Email = ? AND Password = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, hashedPassword);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("Id"));
                user.setName(rs.getString("Name"));
                user.setGender(rs.getString("Gender"));
                user.setEmail(rs.getString("Email"));
                user.setPhone(rs.getString("Phone"));
                user.setAddress(rs.getString("Address"));
                user.setAvatarUrl(rs.getString("Avatar_Url"));
                user.setPassword(rs.getString("Password"));
                user.setRole(rs.getString("Role"));
                user.setStatus(rs.getString("Status"));
                return user;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
                user.setName(rs.getString("Name"));
                user.setGender(rs.getString("Gender"));
                user.setEmail(rs.getString("Email"));
                user.setPhone(rs.getString("Phone"));
                user.setAddress(rs.getString("Address"));
                user.setAvatarUrl(rs.getString("Avatar_Url"));
                user.setPassword(rs.getString("Password"));
                user.setRole(rs.getString("Role"));
                user.setStatus(rs.getString("Status"));
                return user;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

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

}
