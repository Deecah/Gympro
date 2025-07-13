package dao;

import connectDB.ConnectDatabase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FeedbackDAO {

    public boolean insertFeedback(int userId, String type, int referenceId, int point, String content) {
        String sql = "INSERT INTO Feedback (UserID, FeedbackType, ReferenceID, FeedbackContent, Point, Date) "
                   + "VALUES (?, ?, ?, ?, ?, GETDATE())";

        try (Connection conn = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, type);
            ps.setInt(3, referenceId);
            ps.setString(4, content);
            ps.setInt(5, point);

            return ps.executeUpdate() > 0;

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
