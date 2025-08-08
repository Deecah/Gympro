package dao;

import connectDB.ConnectDatabase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Feedback;

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
    public List<Feedback> getFeedbacksByPackageId(int packageId) {
    List<Feedback> feedbackList = new ArrayList<>();
    String sql = "SELECT f.FeedbackContent, f.Point, u.Name, u.AvatarUrl " +
                 "FROM Feedback f JOIN Users u ON f.UserID = u.Id " +
                 "WHERE f.FeedbackType = 'package' AND f.ReferenceID = ?";

    try (Connection conn = ConnectDatabase.getInstance().openConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, packageId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Feedback fb = new Feedback();
            fb.setFeedbackContent(rs.getString("FeedbackContent"));
            fb.setStar(rs.getInt("Point"));
            fb.setUserName(rs.getString("Name"));
            fb.setUserAvatar(rs.getString("AvatarUrl"));
            feedbackList.add(fb);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    return feedbackList;
}
    public List<Feedback> getFeedbacksByTrainerId(int trainerId) {
    List<Feedback> feedbackList = new ArrayList<>();
    String sql = "SELECT f.FeedbackContent, f.Point, u.FullName, u.AvatarUrl " +
                 "FROM Feedback f JOIN Users u ON f.UserID = u.Id " +
                 "WHERE f.FeedbackType = 'trainer' AND f.ReferenceID = ?";

    try (Connection conn = ConnectDatabase.getInstance().openConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, trainerId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Feedback fb = new Feedback();
            fb.setFeedbackContent(rs.getString("FeedbackContent"));
            fb.setStar(rs.getInt("Point"));
            fb.setUserName(rs.getString("FullName"));
            fb.setUserAvatar(rs.getString("AvatarUrl"));
            feedbackList.add(fb);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    return feedbackList;
}
}
