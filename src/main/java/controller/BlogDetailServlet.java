package controller;

import dao.BlogDAO;
import dao.CommentDAO;
import dao.UserDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Blog;
import model.Comment;
import model.User;

@WebServlet(name = "BlogDetailServlet", urlPatterns = {"/BlogDetailServlet"})
public class BlogDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            request.getRequestDispatcher("BlogServlet").forward(request, response);
        }
        switch (action) {
            case "view": {
                Integer id = null;
                try {
                    id = Integer.valueOf(request.getParameter("id"));
                    if (id == null) {
                        request.getRequestDispatcher("BlogServlet").forward(request, response);
                    }
                } catch (NumberFormatException n) {
                    n.printStackTrace();
                }

                BlogDAO blogDAO = new BlogDAO();
                Blog blog = blogDAO.getBlogById(id);
                CommentDAO commentDAO = new CommentDAO();
                
                // Test database connection first
                commentDAO.testDatabaseConnection();
                
                List<Comment> commentList = commentDAO.getAllCommentOfBlog(id);
                
                // Debug: Kiểm tra dữ liệu blog và comment
                System.out.println("=== DEBUG: BlogDetailServlet ===");
                System.out.println("Blog ID: " + id);
                if (blog != null) {
                    System.out.println("Blog Title: " + blog.getTitle());
                    System.out.println("Blog Content: " + blog.getContent());
                } else {
                    System.out.println("Blog is NULL!");
                }
                System.out.println("Comment List Size: " + (commentList != null ? commentList.size() : "NULL"));
                System.out.println("=== END DEBUG: BlogDetailServlet ===");
                
                // Tạo map avatar cho comments
                UserDAO userDAO = new UserDAO();
                Map<Integer, String> commentAvaMap = new HashMap<>();
                for (Comment c : commentList) {
                    if (!commentAvaMap.containsKey(c.getUserId())) {
                        User u = userDAO.getUserById(c.getUserId());
                        if (u != null && u.getAvatarUrl() != null) {
                            commentAvaMap.put(c.getUserId(), u.getAvatarUrl());
                        } else {
                            commentAvaMap.put(c.getUserId(), "img/default-avatar.jpg");
                        }
                    }
                }
                
                // Get current user from session
                User currentUser = (User) request.getSession().getAttribute("user");
                
                request.setAttribute("commentList", commentList);
                request.setAttribute("blog", blog);
                request.setAttribute("commentAvaMap", commentAvaMap);
                request.setAttribute("currentUser", currentUser);
                System.out.println("comment :" +commentList);
                request.getRequestDispatcher("/blog-details.jsp").forward(request, response);
                break;
            }
            case "sendComment":
                break;
            case "reply":
                break;
            case "editComment":
                break;
            case "deleteComment":
                break;
            case "editBlog":
                break;
            case "deleteBlog":
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }
}
