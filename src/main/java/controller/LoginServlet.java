/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import Utils.HashUtil;
import connectDB.ConnectDatabase;
import dao.*;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.sql.*;
import model.Customer;
import model.GoogleAccount;
import model.Trainer;
import model.User;
import controller.GoogleLogin;
import Utils.NotificationUtil;

@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String code = request.getParameter("code");
        if (code != null) {
            handleGoogleLogin(request, response, code);
        } else {
            doPost(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("signin".equals(action)) {
            handleSignin(request, response);
        } else {
            response.sendRedirect("login.jsp");
        }
    }

    private void handleSignin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        byte[] hashedPassword = HashUtil.hashPassword(password);
        try (Connection conn = ConnectDatabase.getInstance().openConnection()) {
            String sql = "SELECT * FROM Users WHERE Email = ? AND Password = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ps.setBytes(2, hashedPassword);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String role = rs.getString("Role");
                String status = rs.getString("Status");
                int userId = rs.getInt("Id");
                if ("Banned".equalsIgnoreCase(status)) {
                    request.setAttribute("error", "Your account is banned.");
                    request.getRequestDispatcher("login.jsp").forward(request, response);
                    return;
                }
                User user = new User();
                user.setUserId(rs.getInt("Id"));
                user.setUserName(rs.getString("Name"));
                user.setEmail(rs.getString("Email"));
                user.setGender(rs.getString("Gender"));
                user.setPhone(rs.getString("Phone"));
                user.setAddress(rs.getString("Address"));
                user.setAvatarUrl(rs.getString("AvatarUrl"));
                user.setRole(role);
                HttpSession session = request.getSession();
                session.setMaxInactiveInterval(3600);
                session.setAttribute("email", email);
                session.setAttribute("user", user);
                session.setAttribute("userId", user.getUserId());
                Cookie cookie = new Cookie("userId",String.valueOf(user.getUserId()));
                Cookie roleCookie = new Cookie("role",role);
                cookie.setMaxAge(60 * 60 * 24 * 7);
                roleCookie.setMaxAge(60 * 60 * 24 * 7);
                response.addCookie(cookie);
                response.addCookie(roleCookie);
                switch (role) {
                    case "Customer":
                        CustomerDAO customerDAO = new CustomerDAO();
                        Customer customer = customerDAO.getProfile(userId);
                        if (customer != null) {
                            session.setAttribute("customer", customer);
                        }
                        session.setAttribute("user", user);
                        // Gửi notification chào mừng
                        NotificationUtil.sendInfoNotification(userId, 
                            "Welcome Back!", 
                            "You have successfully logged in to your account.");
                        response.sendRedirect("index.jsp");
                        break;
                    case "Trainer":
                        TrainerDAO trainerDAO = new TrainerDAO();
                        Trainer trainer = trainerDAO.getProfile(userId);
                        if (trainer != null) {
                            session.setAttribute("trainer", trainer);
                        }
                        session.setAttribute("user", user);
                        // Gửi notification chào mừng trainer
                        NotificationUtil.sendInfoNotification(userId, 
                            "Welcome Back, Trainer!", 
                            "You have successfully logged in to your trainer dashboard.");
                        response.sendRedirect("CustomerServlet");
                        break;
                    case "Admin":
                        response.sendRedirect(request.getContextPath() + "/UserServlet");
                        break;
                    default:
                        response.sendRedirect("login.jsp?error=Unknown role");
                        break;
                }
            } else {
                request.setAttribute("error", "Invalid email or password");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Database error");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

    private void handleGoogleLogin(HttpServletRequest request, HttpServletResponse response, String code)
            throws ServletException, IOException {
        try {
            String accessToken = GoogleLogin.getToken(code);
            GoogleAccount googleAcc = GoogleLogin.getUserInfo(accessToken);

            UserDAO userDao = new UserDAO();

            if (!userDao.isEmailExists(googleAcc.getEmail())) {
                userDao.addUserFromGoogle(googleAcc);
            }

            User user = userDao.getUserByEmail(googleAcc.getEmail());
            if (user == null) {
                request.setAttribute("error", "Login failed. Try again.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
                return;
            }

            if ("Banned".equalsIgnoreCase(user.getStatus())) {
                request.setAttribute("error", "Your account is banned.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
                return;
            }

            HttpSession session = request.getSession();
            session.setMaxInactiveInterval(3600);
            session.setAttribute("userId", user.getUserId());
            session.setAttribute("user", user);
            String role = user.getRole() != null ? user.getRole().trim() : "";
            Cookie cookie = new Cookie("userId",String.valueOf(user.getUserId()));
            Cookie roleCookie = new Cookie("role",role);
            cookie.setMaxAge(60 * 60 * 24 * 7);
            roleCookie.setMaxAge(60 * 60 * 24 * 7);
            response.addCookie(cookie);
            response.addCookie(roleCookie);
            switch (role) {
                case "Customer":
                    CustomerDAO customerDAO = new CustomerDAO();
                    Customer customer = customerDAO.getProfile(user.getUserId());
                    if (customer != null) {
                        session.setAttribute("customer", customer);
                    }
                    response.sendRedirect("index.jsp");
                    break;

                case "Trainer":
                    TrainerDAO trainerDAO = new TrainerDAO();
                    Trainer trainer = trainerDAO.getProfile(user.getUserId());
                    if (trainer != null) {
                        session.setAttribute("trainer", trainer);
                    }
                    response.sendRedirect("trainer/trainer.jsp");
                    break;

                case "Admin":
                    response.sendRedirect("index.html");
                    break;

                default:
                    response.sendRedirect("login.jsp?error=Unknown role");
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Google login failed.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

}