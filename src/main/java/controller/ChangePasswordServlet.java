package controller;

import dao.UserDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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
//      String oldPassword = request.getParameter("oldPassword");

        UserDAO userdao;
        userdao = new UserDAO();
        HttpSession session = request.getSession();
        session.setAttribute("oldPassword", "chanh123");                        //test
        String oldPassword = (String) session.getAttribute("oldPassword");      //test
        session.setAttribute("email", "pchanhdn@gmail.com");
        String email = (String) session.getAttribute("email");
        User u = userdao.getUserByEmail(email);
        if (oldPassword.equals(u.getPassword())) {
            request.getRequestDispatcher("changePassword.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("confirmOldPass.jsp").forward(request, response);
        }
    }

    private void changePassword(HttpServletRequest request, HttpServletResponse response) {
        String password1 = request.getParameter("password1");
        String password2 = request.getParameter("password2");
        if (password1 == null || password2 == null || !password1.equals(password2)) {
            request.setAttribute("error", "Passwords do not match or are missing!!!");
            try {
                request.getRequestDispatcher("changePassword.jsp").forward(request, response); // Quay lại trang nhập mật khẩu
            } catch (ServletException | IOException ex) {
                Logger.getLogger(ChangePasswordServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            return;                                                 // Quan trọng: Thoát khỏi phương thức nếu có lỗi
        } 
            UserDAO userdao;
            userdao = new UserDAO();
            HttpSession session = request.getSession();
            String email = (String) session.getAttribute("email");
            User u = userdao.getUserByEmail(email);
            userdao.updatePassword(u.getId(), password1);
            request.setAttribute("error", "Change Password Successful!!!");
        }
    }
