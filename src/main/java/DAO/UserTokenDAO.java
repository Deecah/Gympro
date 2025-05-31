package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types; // Import Types for setNull
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level; // For logging
import java.util.logging.Logger; // For logging
import connectDB.ConnectDatabase;
import model.UserToken;

public class UserTokenDAO {

    private Connection connection;

    public UserTokenDAO() throws ClassNotFoundException, SQLException { // Thêm SQLException
        this.connection = ConnectDatabase.getInstance().openConnection();
        if (this.connection == null) {
            // Log the error instead of just throwing a RuntimeException
            Logger.getLogger(UserTokenDAO.class.getName()).log(Level.SEVERE, "Failed to establish database connection.");
            throw new SQLException("Failed to connect to the database"); // Throw SQLException for better handling
        }
    }

    public boolean addUserToken(UserToken userToken) throws SQLException {
        String sql = "INSERT INTO UserToken (UserID, Token, TokenType, Expiry, IsUsed, CreatedAt) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) { // Use try-with-resources
            stmt.setInt(1, userToken.getUserId());
            stmt.setString(2, userToken.getToken());
            stmt.setString(3, userToken.getTokenType());
            if (userToken.getExpiry() != null) {
                stmt.setTimestamp(4, Timestamp.valueOf(userToken.getExpiry()));
            } else {
                stmt.setNull(4, Types.TIMESTAMP);
            }

            stmt.setBoolean(5, userToken.isUsed());
            if (userToken.getCreatedAt() != null) {
                stmt.setTimestamp(6, Timestamp.valueOf(userToken.getCreatedAt()));
            } else {
                stmt.setNull(6, Types.TIMESTAMP);
            }

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            Logger.getLogger(UserTokenDAO.class.getName()).log(Level.SEVERE, "Error adding user token: " + userToken.getToken(), e);
            throw e;
        }
    }

    public UserToken getUserToken(int id) throws SQLException {
        String sql = "SELECT * FROM UserToken WHERE ID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new UserToken(
                            rs.getInt("ID"),
                            rs.getInt("UserID"),
                            rs.getString("Token"),
                            rs.getString("TokenType"),
                            rs.getTimestamp("Expiry") != null ? rs.getTimestamp("Expiry").toLocalDateTime() : null, // Check for null
                            rs.getBoolean("IsUsed"),
                            rs.getTimestamp("CreatedAt") != null ? rs.getTimestamp("CreatedAt").toLocalDateTime() : null // Check for null
                    );
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(UserTokenDAO.class.getName()).log(Level.SEVERE, "Error getting user token by ID: " + id, e);
            throw e;
        }
        return null;
    }

    public UserToken getUserTokenbyToken(String token) throws SQLException {
        String sql = "SELECT * FROM UserToken WHERE Token = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, token);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new UserToken(
                            rs.getInt("ID"),
                            rs.getInt("UserID"),
                            rs.getString("Token"),
                            rs.getString("TokenType"),
                            rs.getTimestamp("Expiry") != null ? rs.getTimestamp("Expiry").toLocalDateTime() : null, // Check for null
                            rs.getBoolean("IsUsed"),
                            rs.getTimestamp("CreatedAt") != null ? rs.getTimestamp("CreatedAt").toLocalDateTime() : null // Check for null
                    );
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(UserTokenDAO.class.getName()).log(Level.SEVERE, "Error getting user token by token string: " + token, e);
            throw e;
        }
        return null;
    }

    public List<UserToken> getAllUserTokens() throws SQLException {
        List<UserToken> userTokens = new ArrayList<>();
        String sql = "SELECT * FROM UserToken";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) { // Combine try-with-resources for stmt and rs
            while (rs.next()) {
                userTokens.add(new UserToken(
                        rs.getInt("ID"),
                        rs.getInt("UserID"),
                        rs.getString("Token"),
                        rs.getString("TokenType"),
                        rs.getTimestamp("Expiry") != null ? rs.getTimestamp("Expiry").toLocalDateTime() : null, // Check for null
                        rs.getBoolean("IsUsed"),
                        rs.getTimestamp("CreatedAt") != null ? rs.getTimestamp("CreatedAt").toLocalDateTime() : null // Check for null
                ));
            }
        } catch (SQLException e) {
            Logger.getLogger(UserTokenDAO.class.getName()).log(Level.SEVERE, "Error getting all user tokens", e);
            throw e;
        }
        return userTokens;
    }

    public void updateUserToken(UserToken userToken) throws SQLException {
        String sql = "UPDATE UserToken SET UserID = ?, Token = ?, TokenType = ?, Expiry = ?, IsUsed = ?, CreatedAt = ? WHERE ID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userToken.getUserId());
            stmt.setString(2, userToken.getToken());
            stmt.setString(3, userToken.getTokenType());

            if (userToken.getExpiry() != null) {
                stmt.setTimestamp(4, Timestamp.valueOf(userToken.getExpiry()));
            } else {
                stmt.setNull(4, Types.TIMESTAMP);
            }

            stmt.setBoolean(5, userToken.isUsed());

            if (userToken.getCreatedAt() != null) {
                stmt.setTimestamp(6, Timestamp.valueOf(userToken.getCreatedAt()));
            } else {
                stmt.setNull(6, Types.TIMESTAMP);
            }
            stmt.setInt(7, userToken.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(UserTokenDAO.class.getName()).log(Level.SEVERE, "Error updating user token: " + userToken.getId(), e);
            throw e;
        }
    }
    
    // Thêm phương thức để cập nhật trạng thái IsUsed riêng biệt (được sử dụng trong ResetPasswordServlet)
    public void updateTokenUsedStatus(int userTokenId, boolean isUsed) throws SQLException {
        String sql = "UPDATE UserToken SET IsUsed = ? WHERE ID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBoolean(1, isUsed);
            stmt.setInt(2, userTokenId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(UserTokenDAO.class.getName()).log(Level.SEVERE, "Error updating user token used status for ID: " + userTokenId, e);
            throw e;
        }
    }
    
    // Tùy chọn: Phương thức để vô hiệu hóa tất cả các token "password_reset" đang hoạt động cho một UserID cụ thể
    public void invalidateExistingTokensForUser(int userId, String tokenType) throws SQLException {
        String sql = "UPDATE UserToken SET IsUsed = TRUE WHERE UserID = ? AND TokenType = ? AND IsUsed = FALSE";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, tokenType);
            stmt.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(UserTokenDAO.class.getName()).log(Level.SEVERE, "Error invalidating existing tokens for user " + userId + " type " + tokenType, e);
            throw e;
        }
    }


    public void deleteUserToken(int id) throws SQLException {
        String sql = "DELETE FROM UserToken WHERE ID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(UserTokenDAO.class.getName()).log(Level.SEVERE, "Error deleting user token: " + id, e);
            throw e;
        }
    }
    
    
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            Logger.getLogger(UserTokenDAO.class.getName()).log(Level.SEVERE, "Error closing database connection", e);
        }
    }
}