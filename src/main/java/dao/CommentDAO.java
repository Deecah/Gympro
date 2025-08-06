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
        System.out.println("=== DEBUG: getAllCommentOfBlog ===");
        System.out.println("Blog ID: " + blogId);
        System.out.println("Method called at: " + new java.util.Date());
        
        // Debug: Kiểm tra xem có dữ liệu trong bảng Comment không
        try (Connection conn = ConnectDatabase.getInstance().openConnection()) {
            String checkSql = "SELECT COUNT(*) as total FROM Comment";
            PreparedStatement checkPs = conn.prepareStatement(checkSql);
            ResultSet checkRs = checkPs.executeQuery();
            if (checkRs.next()) {
                System.out.println("Total comments in database: " + checkRs.getInt("total"));
            }
            
            String checkBlogSql = "SELECT COUNT(*) as total FROM Comment WHERE blog_id = ?";
            PreparedStatement checkBlogPs = conn.prepareStatement(checkBlogSql);
            checkBlogPs.setInt(1, blogId);
            ResultSet checkBlogRs = checkBlogPs.executeQuery();
            if (checkBlogRs.next()) {
                System.out.println("Comments for blog " + blogId + ": " + checkBlogRs.getInt("total"));
            }
            
            // Debug: Kiểm tra cấu trúc bảng Comment
            String checkCommentStructure = "SELECT TOP 1 * FROM Comment";
            PreparedStatement checkStructurePs = conn.prepareStatement(checkCommentStructure);
            ResultSet checkStructureRs = checkStructurePs.executeQuery();
            if (checkStructureRs.next()) {
                System.out.println("Comment table structure check:");
                System.out.println("- id: " + checkStructureRs.getInt("id"));
                System.out.println("- user_id: " + checkStructureRs.getInt("user_id"));
                System.out.println("- blog_id: " + checkStructureRs.getInt("blog_id"));
                System.out.println("- content: " + checkStructureRs.getString("content"));
                System.out.println("- created_at: " + checkStructureRs.getTimestamp("created_at"));
                System.out.println("- parent_id: " + checkStructureRs.getObject("parent_id"));
            }
            
            // Debug: Kiểm tra cấu trúc bảng Users
            String checkUsersStructure = "SELECT TOP 1 Id, Name FROM Users";
            PreparedStatement checkUsersPs = conn.prepareStatement(checkUsersStructure);
            ResultSet checkUsersRs = checkUsersPs.executeQuery();
            if (checkUsersRs.next()) {
                System.out.println("Users table structure check:");
                System.out.println("- Id: " + checkUsersRs.getInt("Id"));
                System.out.println("- Name: " + checkUsersRs.getString("Name"));
            }
        } catch (Exception e) {
            System.out.println("ERROR checking comment count: " + e.getMessage());
            e.printStackTrace();
        }
        
        try (Connection conn = ConnectDatabase.getInstance().openConnection()) {
            // Test query trước
            System.out.println("=== TESTING SQL QUERY ===");
            String testSql = "SELECT c.*, u.Name FROM Comment c JOIN Users u ON c.user_id = u.Id";
            System.out.println("Test SQL: " + testSql);
            
            String sql = "SELECT c.*, u.Name FROM Comment c " +
                        "JOIN Users u ON c.user_id = u.Id " +
                        "WHERE c.blog_id = ? ORDER BY c.created_at ASC";
            System.out.println("Main SQL Query: " + sql);
            
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, blogId);
            ResultSet rs = ps.executeQuery();
            
            int commentCount = 0;
            while (rs.next()) { // Dùng while để duyệt qua tất cả các dòng
                try {
                    commentCount++;
                    Comment comment = new Comment();
                    
                    // Lấy dữ liệu từ ResultSet với try-catch cho từng field
                    try {
                        comment.setId(rs.getInt("id"));
                    } catch (Exception e) {
                        System.out.println("ERROR getting id: " + e.getMessage());
                        continue;
                    }
                    
                    try {
                        comment.setUserId(rs.getInt("user_id"));
                    } catch (Exception e) {
                        System.out.println("ERROR getting user_id: " + e.getMessage());
                        continue;
                    }
                    
                    try {
                        comment.setBlogId(rs.getInt("blog_id"));
                    } catch (Exception e) {
                        System.out.println("ERROR getting blog_id: " + e.getMessage());
                        continue;
                    }
                    
                    try {
                        comment.setParentId(rs.getObject("parent_id") != null ? rs.getInt("parent_id") : null);
                    } catch (Exception e) {
                        System.out.println("ERROR getting parent_id: " + e.getMessage());
                        comment.setParentId(null);
                    }
                    
                    try {
                        comment.setContent(rs.getString("content"));
                    } catch (Exception e) {
                        System.out.println("ERROR getting content: " + e.getMessage());
                        continue;
                    }
                    
                    try {
                        comment.setCreatedAt(rs.getTimestamp("created_at"));
                    } catch (Exception e) {
                        System.out.println("ERROR getting created_at: " + e.getMessage());
                        continue;
                    }
                    
                    try {
                        comment.setUserName(rs.getString("Name"));
                    } catch (Exception e) {
                        System.out.println("ERROR getting Name: " + e.getMessage());
                        comment.setUserName("Unknown");
                    }
                    
                    // Debug: In ra thông tin từng comment
                    System.out.println("--- Comment " + commentCount + " ---");
                    System.out.println("ID: " + comment.getId());
                    System.out.println("User ID: " + comment.getUserId());
                    System.out.println("Blog ID: " + comment.getBlogId());
                    System.out.println("Parent ID: " + comment.getParentId());
                    System.out.println("Content: " + comment.getContent());
                    System.out.println("Created At: " + comment.getCreatedAt());
                    System.out.println("User Name: " + comment.getUserName());
                    System.out.println("------------------------");
                    
                    commentList.add(comment);
                } catch (Exception e) {
                    System.out.println("ERROR processing comment " + commentCount + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }
            
            System.out.println("Total comments found: " + commentCount);
            System.out.println("CommentList size: " + commentList.size());
            
        } catch (Exception e) {
            System.out.println("ERROR in getAllCommentOfBlog: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("=== END DEBUG: getAllCommentOfBlog ===");
        return commentList;
    }
    
    // Lấy danh sách comment theo blogId (gồm cả comment gốc và trả lời)
    public List<Comment> getCommentsByBlogId(int blogId) {
        String sql = "SELECT c.*, u.Name FROM Comment c " +
                    "JOIN Users u ON c.user_id = u.Id " +
                    "WHERE c.blog_id = ? ORDER BY c.created_at ASC";
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
                c.setUserId(rs.getInt("user_id"));
                c.setBlogId(rs.getInt("blog_id"));
                c.setContent(rs.getString("content"));
                c.setCreatedAt(rs.getTimestamp("created_at"));
                c.setParentId(rs.getObject("parent_id") != null ? rs.getInt("parent_id") : null);
                c.setUserName(rs.getString("Name"));
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
        System.out.println("=== DEBUG: addComment ===");
        System.out.println("User ID: " + comment.getUserId());
        System.out.println("Blog ID: " + comment.getBlogId());
        System.out.println("Content: " + comment.getContent());
        System.out.println("Parent ID: " + comment.getParentId());
        
        String sql = "INSERT INTO Comment(user_id, blog_id, content, parent_id) VALUES (?, ?, ?, ?)";
        System.out.println("SQL Query: " + sql);
        
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
            
            System.out.println("Parameters set successfully");
            int result = stmt.executeUpdate();
            System.out.println("Execute Update Result: " + result);
            System.out.println("=== END DEBUG: addComment ===");
            return result > 0;

        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("ERROR in addComment: " + e.getMessage());
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
    String sql = "UPDATE Comment SET content = ?, created_at = ? WHERE id = ?";
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


    // Test method để kiểm tra database connection
    public void testDatabaseConnection() {
        System.out.println("=== TEST DATABASE CONNECTION ===");
        System.out.println("Test started at: " + new java.util.Date());
        
        try (Connection conn = ConnectDatabase.getInstance().openConnection()) {
            System.out.println("Database connection successful!");
            
            // Test Comment table
            String testCommentSql = "SELECT COUNT(*) as count FROM Comment";
            PreparedStatement testPs = conn.prepareStatement(testCommentSql);
            ResultSet testRs = testPs.executeQuery();
            if (testRs.next()) {
                System.out.println("Comment table exists, count: " + testRs.getInt("count"));
            }
            
            // Test Users table
            String testUsersSql = "SELECT COUNT(*) as count FROM Users";
            PreparedStatement testUsersPs = conn.prepareStatement(testUsersSql);
            ResultSet testUsersRs = testUsersPs.executeQuery();
            if (testUsersRs.next()) {
                System.out.println("Users table exists, count: " + testUsersRs.getInt("count"));
            }
            
            // Test simple query without JOIN first
            String simpleSql = "SELECT TOP 1 * FROM Comment";
            System.out.println("Testing simple query: " + simpleSql);
            PreparedStatement simplePs = conn.prepareStatement(simpleSql);
            ResultSet simpleRs = simplePs.executeQuery();
            if (simpleRs.next()) {
                System.out.println("Simple query successful!");
                System.out.println("Comment ID: " + simpleRs.getInt("id"));
                System.out.println("User ID: " + simpleRs.getInt("user_id"));
                System.out.println("Blog ID: " + simpleRs.getInt("blog_id"));
                System.out.println("Content: " + simpleRs.getString("content"));
                
                // Test JOIN query với user_id cụ thể
                int userId = simpleRs.getInt("user_id");
                String testJoinSql = "SELECT TOP 1 u.Name FROM Users u WHERE u.Id = ?";
                System.out.println("Testing JOIN query with user_id " + userId + ": " + testJoinSql);
                PreparedStatement testJoinPs = conn.prepareStatement(testJoinSql);
                testJoinPs.setInt(1, userId);
                ResultSet testJoinRs = testJoinPs.executeQuery();
                if (testJoinRs.next()) {
                    System.out.println("JOIN query successful!");
                    System.out.println("User Name: " + testJoinRs.getString("Name"));
                } else {
                    System.out.println("No user found for user_id: " + userId);
                }
            } else {
                System.out.println("No data found in simple query - Comment table might be empty");
            }
            
        } catch (Exception e) {
            System.out.println("ERROR in testDatabaseConnection: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("=== END TEST DATABASE CONNECTION ===");
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