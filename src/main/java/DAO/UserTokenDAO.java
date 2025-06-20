package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import connectDB.ConnectDatabase;
import model.UserToken;

public class UserTokenDAO {

    private Connection connection;

    public UserTokenDAO() throws ClassNotFoundException {
        this.connection = ConnectDatabase.getInstance().openConnection();
        if (this.connection == null) {
            throw new RuntimeException("Failed to connect to the database");
        }
    }

    public boolean addUserToken(UserToken userToken) throws SQLException {
        String sql = "INSERT INTO UserToken (UserID, Token, TokenType, Expiry, IsUsed, CreatedAt) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, userToken.getUserId());
            stmt.setString(2, userToken.getToken());
            stmt.setString(3, userToken.getTokenType());
            stmt.setTimestamp(4, Timestamp.valueOf(userToken.getExpiry())); // Chuyển LocalDateTime thành Timestamp
            stmt.setBoolean(5, userToken.isUsed());
            stmt.setTimestamp(6, Timestamp.valueOf(userToken.getCreatedAt())); // Chuyển LocalDateTime thành Timestamp
            stmt.executeUpdate();
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println(e);
        }
        return false;

    }

    public UserToken getUserToken(int id) throws SQLException {
        String sql = "SELECT * FROM UserToken WHERE ID = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return new UserToken(
                    rs.getInt("ID"),
                    rs.getInt("UserID"),
                    rs.getString("Token"),
                    rs.getString("TokenType"),
                    rs.getTimestamp("Expiry").toLocalDateTime(), // Chuyển Timestamp thành LocalDateTime
                    rs.getBoolean("IsUsed"),
                    rs.getTimestamp("CreatedAt").toLocalDateTime() // Chuyển Timestamp thành LocalDateTime
            );
        }
        return null;
    }

    public List<UserToken> getAllUserTokens() throws SQLException {
        List<UserToken> userTokens = new ArrayList<>();
        String sql = "SELECT * FROM UserToken";
        PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            userTokens.add(new UserToken(
                    rs.getInt("ID"),
                    rs.getInt("UserID"),
                    rs.getString("Token"),
                    rs.getString("TokenType"),
                    rs.getTimestamp("Expiry").toLocalDateTime(),
                    rs.getBoolean("IsUsed"),
                    rs.getTimestamp("CreatedAt").toLocalDateTime()
            ));
        }
        return userTokens;
    }

    public void updateUserToken(UserToken userToken) throws SQLException {
        String sql = "UPDATE UserToken SET UserID = ?, Token = ?, TokenType = ?, Expiry = ?, IsUsed = ?, CreatedAt = ? WHERE ID = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, userToken.getUserId());
        stmt.setString(2, userToken.getToken());
        stmt.setString(3, userToken.getTokenType());
        stmt.setTimestamp(4, Timestamp.valueOf(userToken.getExpiry()));
        stmt.setBoolean(5, userToken.isUsed());
        stmt.setTimestamp(6, Timestamp.valueOf(userToken.getCreatedAt()));
        stmt.setInt(7, userToken.getId());
        stmt.executeUpdate();
    }

    public void deleteUserToken(int id) throws SQLException {
        String sql = "DELETE FROM UserToken WHERE ID = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.executeUpdate();
    }
}
