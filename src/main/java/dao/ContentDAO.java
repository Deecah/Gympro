package dao;

import connectDB.ConnectDatabase;
import model.Content;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContentDAO {

    public static boolean insert(Content content) {
        String sql = "INSERT INTO Content (title, body) VALUES (?, ?)";
        try (Connection conn = ConnectDatabase.getInstance().openConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, content.getTitle());
            stmt.setString(2, content.getBody());

            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace(); // ✅ Phải in ra để xem có lỗi không
        }
        return false;
    }

    public static List<Content> getAll() {
        List<Content> list = new ArrayList<>();
        String sql = "SELECT * FROM Content ORDER BY id DESC";
        try (Connection conn = ConnectDatabase.getInstance().openConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Content c = new Content(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("body")
                );
                list.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static boolean update(Content content) {
        String sql = "UPDATE Content SET title = ?, body = ? WHERE id = ?";
        try (Connection conn = ConnectDatabase.getInstance().openConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, content.getTitle());
            stmt.setString(2, content.getBody());
            stmt.setInt(3, content.getId());

            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean delete(int id) {
        String sql = "DELETE FROM Content WHERE id = ?";
        try (Connection conn = ConnectDatabase.getInstance().openConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
