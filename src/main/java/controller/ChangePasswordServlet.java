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

@WebServlet(name="ChangePasswordServlet", urlPatterns={"/ChangePasswordServlet"})
public class ChangePasswordServlet extends HttpServlet {

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

    private void confirmPassword(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String oldPassword = request.getParameter("oldPassword");
        HttpSession session = request.getSession();
        String email = (String) session.getAttribute("email");
        UserDAO userDAO = new UserDAO();
        User u = userDAO.getUserByEmail(email);
        if (u == null) {
            System.out.println("DEBUG: User object (u) is null. Email: " + email);
            request.setAttribute("mess", "Phiên làm việc đã hết hạn hoặc tài khoản không tồn tại. Vui lòng đăng nhập lại.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return; // Dừng xử lý nếu u là null
        }
        byte[] password = u.getPassword();
        byte[] oldPasswordHashed = HashUtil.hashPassword(oldPassword);
        if (Arrays.equals(oldPasswordHashed, password)) {
            System.out.println(Arrays.toString(u.getPassword()));
            System.out.println(Arrays.toString(oldPasswordHashed));
            request.getRequestDispatcher("changePassword.jsp").forward(request, response);
        } else {
            System.out.println(Arrays.toString(u.getPassword()));
            System.out.println(Arrays.toString(oldPasswordHashed));
            request.setAttribute("mess", "Wrong password!!!!");
            request.getRequestDispatcher("confirmOldPass.jsp").forward(request, response);
        }
    }

    private void changePassword(HttpServletRequest request, HttpServletResponse response) {
        String password1 = request.getParameter("password1");
        String password2 = request.getParameter("password2");
        HttpSession session = request.getSession();
        User u = (User) session.getAttribute("user");
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
        UserDAO userDAO = new UserDAO();
        userDAO.updatePassword(u.getUserId(), newPasswordHashed);
        request.setAttribute("mess", "Change Password Successful!!!");
    }
}