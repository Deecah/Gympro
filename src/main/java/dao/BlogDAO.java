package dao;

import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Blog;
import connectDB.ConnectDatabase;
import java.time.format.DateTimeFormatter;

public class BlogDAO {

// Lấy 1 bài blog theo id
    public Blog getBlogById(int id) {
        String sql = "SELECT * FROM Blog WHERE id = ?";
        ConnectDatabase db = ConnectDatabase.getInstance();
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            con = db.openConnection();
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                Blog blog = new Blog();
                blog.setId(rs.getInt("id"));
                blog.setTitle(rs.getString("title"));
                blog.setContent(rs.getString("content"));
                blog.setThumbnail(rs.getString("thumbnail"));
                blog.setTag(rs.getString("tag"));
                blog.setCreatedAt(rs.getTimestamp("createdAt"));
                blog.setImagesUrl(getImagesByBlogId(id)); // ảnh phụ
                return blog;
            }

        } catch (ClassNotFoundException | SQLException e) {
            Logger.getLogger(BlogDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            close(con, stmt, rs);
        }

        return null;
    }

    // Lấy tất cả blog
    public ArrayList<Blog> getAllBlogs() {
        String sql = "SELECT * FROM Blog ORDER BY createdAt DESC";
        ArrayList<Blog> list = new ArrayList<>();
        ConnectDatabase db = ConnectDatabase.getInstance();
        try (Connection con = db.openConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            while (rs.next()) {
                
                Blog blog = new Blog();
                blog.setId(rs.getInt("id"));
                blog.setTitle(rs.getString("title"));
                blog.setContent(rs.getString("content"));
                blog.setThumbnail(rs.getString("thumbnail"));
                blog.setTag(rs.getString("tag"));
                blog.setCreatedAt(rs.getTimestamp("createdAt"));
                blog.setImagesUrl(getImagesByBlogId(blog.getId())); // ảnh phụ
                list.add(blog);
            }

        } catch (ClassNotFoundException | SQLException e) {
            Logger.getLogger(BlogDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return list;
    }

    // Thêm mới 1 blog
    public boolean addBlog(Blog blog) {
        String sql = "INSERT INTO Blog (title, content, thumbnail, tag, createdAt) VALUES (?, ?, ?, ?, GETDATE())";
        ConnectDatabase db = ConnectDatabase.getInstance();
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            con = db.openConnection();
            stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, blog.getTitle());
            stmt.setString(2, blog.getContent());
            stmt.setString(3, blog.getThumbnail());
            stmt.setString(4, blog.getTag());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    int blogId = rs.getInt(1);
                    blog.setId(blogId);
                    // thêm ảnh phụ nếu có
                    if (blog.getImagesUrl() != null) {
                        for (String img : blog.getImagesUrl()) {
                            addImage(blogId, img);
                        }
                    }
                }
                return true;
            }

        } catch (ClassNotFoundException | SQLException e) {
            Logger.getLogger(BlogDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            close(con, stmt, rs);
        }
        return false;
    }

    // Cập nhật blog
    public boolean updateBlog(Blog blog) {
        String sql = "UPDATE Blog SET title = ?, content = ?, thumbnail = ?, tag = ? WHERE id = ?";
        ConnectDatabase db = ConnectDatabase.getInstance();
        Connection con = null;
        PreparedStatement stmt = null;

        try {
            con = db.openConnection();
            stmt = con.prepareStatement(sql);
            stmt.setString(1, blog.getTitle());
            stmt.setString(2, blog.getContent());
            stmt.setString(3, blog.getThumbnail());
            stmt.setString(4, blog.getTag());
            stmt.setInt(5, blog.getId());

            boolean updated = stmt.executeUpdate() > 0;

            // cập nhật ảnh phụ: xóa cũ → thêm lại
            if (updated) {
                deleteImagesByBlogId(blog.getId());
                if (blog.getImagesUrl() != null) {
                    for (String img : blog.getImagesUrl()) {
                        addImage(blog.getId(), img);
                    }
                }
            }

            return updated;

        } catch (ClassNotFoundException | SQLException e) {
            Logger.getLogger(BlogDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            close(con, stmt, null);
        }
        return false;
    }

    // Xoá blog (và ảnh phụ)
    public boolean deleteBlog(int id) {
        deleteImagesByBlogId(id);
        String sql = "DELETE FROM Blog WHERE id = ?";
        ConnectDatabase db = ConnectDatabase.getInstance();
        try (Connection con = db.openConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (ClassNotFoundException | SQLException e) {
            Logger.getLogger(BlogDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return false;
    }

    // ======= Phần xử lý ảnh phụ =======

    public ArrayList<String> getImagesByBlogId(int blogId) {
        String sql = "SELECT imageUrl FROM BlogImage WHERE blogId = ?";
        ArrayList<String> list = new ArrayList<>();
        ConnectDatabase db = ConnectDatabase.getInstance();
        try (Connection con = db.openConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, blogId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(rs.getString("imageUrl"));
            }

        } catch (ClassNotFoundException | SQLException e) {
            Logger.getLogger(BlogDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return list;
    }

    public boolean addImage(int blogId, String imageUrl) {
        String sql = "INSERT INTO BlogImage (blogId, imageUrl) VALUES (?, ?)";
        ConnectDatabase db = ConnectDatabase.getInstance();
        try (Connection con = db.openConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, blogId);
            stmt.setString(2, imageUrl);
            return stmt.executeUpdate() > 0;

        } catch (ClassNotFoundException | SQLException e) {
            Logger.getLogger(BlogDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return false;
    }

    public boolean deleteImagesByBlogId(int blogId) {
        String sql = "DELETE FROM BlogImage WHERE blogId = ?";
        ConnectDatabase db = ConnectDatabase.getInstance();
        try (Connection con = db.openConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, blogId);
            stmt.executeUpdate();
        } catch (ClassNotFoundException | SQLException e) {
            Logger.getLogger(BlogDAO.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
        return true;
    }

    // ======= Hàm đóng kết nối =======
    private void close(Connection con, PreparedStatement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (con != null) con.close();
        } catch (SQLException ex) {
            Logger.getLogger(BlogDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
