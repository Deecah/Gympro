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
import jakarta.servlet.http.*;
import java.sql.*;
import model.Customer;
import model.GoogleAccount;
import model.Trainer;
import model.User;

public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String code = request.getParameter("code");
        if (code != null) {
            handleGoogleLogin(request, response, code);
        } else {
            doPost(request, response); // fallback cho các action khác
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
                user.setUserId(userId);
                user.setUserName(rs.getString("Name"));
                user.setEmail(rs.getString("Email"));
                user.setGender(rs.getString("Gender"));
                user.setPhone(rs.getString("Phone"));
                user.setAddress(rs.getString("Address"));
                user.setAvatarUrl(rs.getString("AvatarUrl"));
                user.setRole(role);

                HttpSession session = request.getSession();
                session.setMaxInactiveInterval(3600);
                session.setAttribute("user", user);
                switch (role) {
                    case "Customer":
                        CustomerDAO customerDAO = new CustomerDAO();
                        Customer customer = customerDAO.getProfile(userId);
                        if (customer != null) {
                            session.setAttribute("customer", customer);
                        }
                        response.sendRedirect("index.jsp");
                        break;
                    case "Trainer":
                        TrainerDAO trainerDAO = new TrainerDAO();
                        Trainer trainer = trainerDAO.getProfile(userId);
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
            session.setAttribute("user", user);

            // Vì mặc định là Customer, nên load thêm thông tin từ CustomerDAO
            if ("Customer".equalsIgnoreCase(user.getRole())) {
                CustomerDAO customerDAO = new CustomerDAO();
                Customer customer = customerDAO.getProfile(user.getUserId());
                if (customer != null) {
                    session.setAttribute("customer", customer);
                }
                response.sendRedirect("index.jsp");
            } else {
                switch (user.getRole()) {
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
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Google login failed.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

}
