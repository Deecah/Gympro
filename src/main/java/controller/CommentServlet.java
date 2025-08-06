
package controller;

import dao.CommentDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Comment;
import model.User;

@WebServlet(name = "CommentServlet", urlPatterns = {"/CommentServlet"})
public class CommentServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
         if(user==null){
                    response.sendRedirect("LoginServlet");
                }
        switch (action) {
            case "send": {
                // Debug: Kiểm tra dữ liệu từ form
                System.out.println("=== DEBUG: CommentServlet - SEND ===");
                System.out.println("User ID: " + user.getUserId());
                System.out.println("User Name: " + user.getUserName());
                
                String content = request.getParameter("content");
                System.out.println("Content: " + content);
                
                Integer blogId = Integer.valueOf(request.getParameter("blogId"));
                System.out.println("Blog ID: " + blogId);
                
                Comment comment = new Comment();
                comment.setUserId(user.getUserId());
                comment.setBlogId(blogId);
                comment.setContent(content);
                Integer parentId = null;
                comment.setParentId(parentId);
                
                // Debug: Kiểm tra comment object
                System.out.println("Comment Object:");
                System.out.println("- User ID: " + comment.getUserId());
                System.out.println("- Blog ID: " + comment.getBlogId());
                System.out.println("- Content: " + comment.getContent());
                System.out.println("- Parent ID: " + comment.getParentId());
                System.out.println("=== END DEBUG: CommentServlet - SEND ===");

                CommentDAO commentDAO = new CommentDAO();
                boolean result = commentDAO.addComment(comment);
                System.out.println("Add comment result: " + result);
                
                if (result) {
                    NotificationServlet sendNoti = new NotificationServlet();
                    sendNoti.sendPopupNotification("Send Comment success");
                    // Redirect back to blog detail page
                    response.sendRedirect("BlogDetailServlet?action=view&id=" + blogId);
                } else {
                    NotificationServlet sendNoti = new NotificationServlet();
                    sendNoti.sendPopupNotification("Sent comment fail!!. Please try again later!");
                    // Redirect back to blog detail page even if failed
                    response.sendRedirect("BlogDetailServlet?action=view&id=" + blogId);
                }
                break;
            }

            case "reply": {
                String content = request.getParameter("content");
                Integer blogId = Integer.valueOf(request.getParameter("blogId"));
                Comment comment = new Comment();
                comment.setUserId(user.getUserId());
                comment.setBlogId(blogId);
                comment.setContent(content);
                Integer parentId = Integer.valueOf(request.getParameter("parentId"));
                comment.setParentId(parentId);

                CommentDAO commentDAO = new CommentDAO();
                if (commentDAO.addComment(comment)) {
                    NotificationServlet sendNoti = new NotificationServlet();
                    sendNoti.sendPopupNotification("Sent comment successfully!!");
                } else {
                    NotificationServlet sendNoti = new NotificationServlet();
                    sendNoti.sendPopupNotification("Sent comment fail!!. Please try again later!");
                }
                break;
            }
            case "edit":{
                
                break;}
            case "delete":{
                CommentDAO commentDAO = new CommentDAO();
                
                break;}
        }

    }

}

