package controller;

import dao.UserDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.User;

public class ConfirmOldPassServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ConfirmOldPassServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ConfirmOldPassServlet at " + request.getContextPath () + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        request.getRequestDispatcher("ConfirmOldPass.jsp").forward(request, response);
        
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
         String oldPassword = (String) request.getAttribute("oldPassword");
        UserDAO userdao;
        userdao = new UserDAO();
        HttpSession session = request.getSession();
        String email = (String) session.getAttribute("email");
        User u = userdao.getUserByEmail(email);
        if(oldPassword.equals(u.getPassword())){
            request.getRequestDispatcher("changePassword.jsp").forward(request, response);
        }else{
            request.getRequestDispatcher("confirmOldPass.jsp").forward(request, response);
        }
        
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}