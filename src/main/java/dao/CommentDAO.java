package dao;

import connectDB.ConnectDatabase;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.List;
import model.Comment;

public class CommentDAO {

     public ArrayList<Comment> getAllCommentOfBlog(int blogId) {
        ArrayList<Comment> commentList = new ArrayList<>(); // Khởi tạo ArrayList
        try (Connection conn = ConnectDatabase.getInstance().openConnection()) {
            String sql = "SELECT * FROM Comment";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) { // Dùng while để duyệt qua tất cả các dòng
                Comment comment = new Comment();
                comment.setId(rs.getInt("id"));
                comment.setUserId(rs.getInt("user_id"));
                comment.setBlogId(rs.getInt("blog_id"));
                comment.setParentId(rs.getInt("parent_id"));
                comment.setContent(rs.getString("content"));
                comment.setCreatedAt(rs.getTimestamp("created_at"));
                commentList.add(comment);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return commentList;
    }
    
    // Lấy danh sách comment theo blogId (gồm cả comment gốc và trả lời)
    public List<Comment> getCommentsByBlogId(int blogId) {
        String sql = "SELECT * FROM Comment WHERE blogId = ? ORDER BY createdAt ASC";
        List<Comment> list = new ArrayList<>();
        ConnectDatabase db = ConnectDatabase.getInstance();
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            con = db.openConnection();
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, blogId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Comment c = new Comment();
                c.setId(rs.getInt("id"));
                c.setUserId(rs.getInt("userId"));
                c.setBlogId(rs.getInt("blogId"));
                c.setContent(rs.getString("content"));
                c.setCreatedAt(rs.getTimestamp("createdAt"));
                c.setParentId(rs.getObject("parentId") != null ? rs.getInt("parentId") : null);
                list.add(c);
            }

        } catch (ClassNotFoundException | SQLException e) {
            Logger.getLogger(CommentDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            close(con, stmt, rs);
        }

        return list;
    }

    // Thêm bình luận mới
    public boolean addComment(Comment comment) {
        String sql = "INSERT INTO Comment(userId, blogId, content, parentId) VALUES (?, ?, ?, ?)";
        ConnectDatabase db = ConnectDatabase.getInstance();
        Connection con = null;
        PreparedStatement stmt = null;

        try {
            con = db.openConnection();
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, comment.getUserId());
            stmt.setInt(2, comment.getBlogId());
            stmt.setString(3, comment.getContent());
            if (comment.getParentId() != null) {
                stmt.setInt(4, comment.getParentId());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }

            return stmt.executeUpdate() > 0;

        } catch (ClassNotFoundException | SQLException e) {
            Logger.getLogger(CommentDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            close(con, stmt, null);
        }

        return false;
    }

    // Xóa comment theo id
    public boolean deleteCommentById(int id) {
        String sql = "DELETE FROM Comment WHERE id = ?";
        ConnectDatabase db = ConnectDatabase.getInstance();
        Connection con = null;
        PreparedStatement stmt = null;

        try {
            con = db.openConnection();
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (ClassNotFoundException | SQLException e) {
            Logger.getLogger(CommentDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            close(con, stmt, null);
        }

        return false;
    }
    
    public boolean updateComment(Comment comment) {
    String sql = "UPDATE Comment SET content = ?, createdAt = ? WHERE id = ?";
    ConnectDatabase db = ConnectDatabase.getInstance();
    Connection con = null;
    PreparedStatement stmt = null;

    try {
        con = db.openConnection();
        stmt = con.prepareStatement(sql);
        stmt.setString(1, comment.getContent());
        stmt.setTimestamp(2, comment.getCreatedAt());
        stmt.setInt(3, comment.getId());

        return stmt.executeUpdate() > 0;

    } catch (ClassNotFoundException | SQLException e) {
        Logger.getLogger(CommentDAO.class.getName()).log(Level.SEVERE, null, e);
    } finally {
        close(con, stmt, null);
    }

    return false;
}


    // Hàm đóng kết nối
    private void close(Connection con, PreparedStatement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (con != null) con.close();
        } catch (SQLException e) {
            Logger.getLogger(CommentDAO.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}