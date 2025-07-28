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
        switch (action) {
            case "send": {
                String content = request.getParameter("content");
                Integer blogId = Integer.valueOf(request.getParameter("blogId"));
                Comment comment = new Comment();
                comment.setUserId(user.getUserId());
                comment.setBlogId(blogId);
                comment.setContent(content);
                Integer parentId = null;
                comment.setParentId(parentId);

                CommentDAO commentDAO = new CommentDAO();
                if (commentDAO.addComment(comment)) {
                    NotificationServlet sendNoti = new NotificationServlet();
                    sendNoti.sendPopupNotification("Send Comment success");
                } else {
                    NotificationServlet sendNoti = new NotificationServlet();
                    sendNoti.sendPopupNotification("Sent comment fail!!. Please try again later!");
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
