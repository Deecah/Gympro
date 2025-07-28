package controller;

import dao.NotificationDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Notification;
import model.User;
import jakarta.servlet.annotation.WebServlet;
@WebServlet("/send-test-notification")

public class NotificationServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String msg = request.getParameter("msg");
        websocket.NotificationEndPoint.broadcast(msg);
        response.getWriter().write("Đã gửi thông báo.");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String action = request.getParameter("action");
        if ("open".equals(action)) {
            try {
                NotificationDAO notiDAO = new NotificationDAO();
                User user = (User) session.getAttribute("user");
                List<Notification> notifications = notiDAO.getNotificationsByUserId(user.getUserId());
                session.setAttribute("notifications", notifications);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(NotificationServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    public void sendPopupNotification(String msg) throws IOException{
        websocket.NotificationEndPoint.broadcast(msg);
        System.out.println("sent popup");
    }
    
    public void sendNotification(int userId ,String title, String content, String msg, HttpServletResponse response ) throws ClassNotFoundException, ClassNotFoundException, IOException{
        NotificationDAO notiDAO = new NotificationDAO();
        if(notiDAO.addNotification(userId, title, content)){
            sendPopupNotification(msg);
        }else{
            sendPopupNotification("Something wrong happened. Try again later!!!");
        }
    }
    
    public void sendNotificationAllUser(String title, String content) throws ClassNotFoundException{
    NotificationDAO notiDAO = new NotificationDAO();
    notiDAO.addNotificationToAllUsers(title, content);
    
}
}
