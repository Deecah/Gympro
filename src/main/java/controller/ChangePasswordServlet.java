package controller;

import Utils.HashUtil;
import dao.UserDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.User;

public class ChangePasswordServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ChangePasswordServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ChangePasswordServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("confirmOldPass.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if ("confirm".equals(action)) {
            confirmPassword(request, response);
        }
        if ("changePassword".equals(action)) {
            changePassword(request, response);
        }

    }
    
    @Override
    public String getServletInfo() {
        return "Short description";
    }

    private void confirmPassword(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String oldPassword = request.getParameter("oldPassword");
        HttpSession session = request.getSession();
        int userId = (int) session.getAttribute("userId");
        UserDAO userdao = new UserDAO();
        User u = userdao.getUserById(userId);
        byte[] oldPasswordHashed = HashUtil.hashPassword(oldPassword);
        if (Arrays.equals(oldPasswordHashed, u.getPassword())) {
            request.getRequestDispatcher("changePassword.jsp").forward(request, response);
        } else {
            request.setAttribute("mess", "Wrong password!!!!");
            request.getRequestDispatcher("confirmOldPass.jsp").forward(request, response);
        }
    }

    private void changePassword(HttpServletRequest request, HttpServletResponse response) {
        String password1 = request.getParameter("password1");
        String password2 = request.getParameter("password2");
        HttpSession session = request.getSession();
        int userId = (int) session.getAttribute("userId");
        UserDAO userdao = new UserDAO();
        User u = userdao.getUserById(userId);
        if (password1 == null || password2 == null || !password1.equals(password2)) {

            try {
                request.setAttribute("mess", "Passwords do not match or are missing!!!");
                request.getRequestDispatcher("changePassword.jsp").forward(request, response);
            } catch (ServletException | IOException ex) {
                Logger.getLogger(ChangePasswordServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            return;
        }
        byte[] newPasswordHashed = HashUtil.hashPassword(password1);
        userdao.updatePassword(u.getUserId(), newPasswordHashed);
        request.setAttribute("mess", "Change Password Successful!!!");
    }


}
