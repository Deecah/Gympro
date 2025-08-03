package main;

import dao.CommentDAO;
import model.Comment;
import java.util.List;

public class TestCommentDAO {
    public static void main(String[] args) {
        System.out.println("=== TESTING COMMENT DAO ===");
        
        CommentDAO commentDAO = new CommentDAO();
        
        // Test database connection
        commentDAO.testDatabaseConnection();
        
        // Test getting comments for blog ID 1
        System.out.println("\n=== TESTING getAllCommentOfBlog ===");
        List<Comment> comments = commentDAO.getAllCommentOfBlog(1);
        System.out.println("Comments found: " + comments.size());
        
        for (Comment comment : comments) {
            System.out.println("Comment ID: " + comment.getId());
            System.out.println("User ID: " + comment.getUserId());
            System.out.println("Blog ID: " + comment.getBlogId());
            System.out.println("Content: " + comment.getContent());
            System.out.println("User Name: " + comment.getUserName());
            System.out.println("---");
        }
        
        System.out.println("=== END TESTING COMMENT DAO ===");
    }
} 