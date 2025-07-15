package controller;

import dao.NotificationDAO;
import java.io.IOException;
import java.io.PrintWriter;
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

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet NotificationServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet NotificationServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

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
    
    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
