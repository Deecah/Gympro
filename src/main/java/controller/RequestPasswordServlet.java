/*package controller;

import dao.UserDAO;
import dao.UserTokenDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.User;
import model.UserToken;

public class RequestPasswordServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ForgotPasswordServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ForgotPasswordServlet at " + request.getContextPath () + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        request.getRequestDispatcher("requestPassword.jsp").forward(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        User u;
        try {
            String email = request.getParameter("email");
            UserDAO uDAO = new UserDAO();
            
            u = uDAO.getUserByEmail(email);
            if(u == null){
            request.setAttribute("mess", "Email not exist!!!");
            request.getRequestDispatcher("requestPassword.jsp").forward(request, response);
        }
            resetService rService = new resetService();
            String token = rService.generateToken();
            String linkReset = "http://localhost:8080/SWP391/ResetPassword?token="+token;
            UserToken userToken = new UserToken(u.getUserId(), token, "password_reset",rService.expireDateTime(), false, LocalDateTime.now());
            UserTokenDAO userTokenDAO = new UserTokenDAO();
            
            boolean isInsert = userTokenDAO.addUserToken(userToken);
          
            if(!isInsert){
                request.setAttribute("mess", "Something wrong:<");
                request.getRequestDispatcher("requestPassword.jsp").forward(request, response);
                return;
            }
            
            boolean isSend = rService.sendEmail(email, linkReset);
            if(!isSend){
                request.setAttribute("mess", "Something:<");
                request.getRequestDispatcher("requestPassword.jsp").forward(request, response);
                return;
            }
            
            request.setAttribute("mess", "Send request success");
            request.getRequestDispatcher("requestPassword.jsp").forward(request, response);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RequestPasswordServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(RequestPasswordServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}*/