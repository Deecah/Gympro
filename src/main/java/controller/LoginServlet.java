/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import Utils.HashUtil;
import connectDB.ConnectDatabase;
import dao.UserDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import model.GoogleAccount;
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
        if ("signup".equals(action)) {
            handleSignup(request, response);
        } else if ("signin".equals(action)) {
            handleSignin(request, response);
        } else {
            response.sendRedirect("login.jsp");
        }
    }

    private void handleSignup(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String role = request.getParameter("role"); // lấy role từ form
        byte[] hashedPassword = HashUtil.hashPassword(password);
        try (Connection conn = ConnectDatabase.getInstance().openConnection()) {
            String sql = "INSERT INTO Users (Name, Email, Password, Role, Status) VALUES (?, ?, ?, ?, 'Normal')";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setBytes(3, hashedPassword);
            ps.setString(4, role);
            ps.executeUpdate();
            request.setAttribute("message", "Registration successful. Please login.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Email already exists or database error.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
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
                if ("Banned".equalsIgnoreCase(status)) {
                    request.setAttribute("error", "Your account is banned.");
                    request.getRequestDispatcher("login.jsp").forward(request, response);
                    return;
                }
                HttpSession session = request.getSession();
                session.setMaxInactiveInterval(3600);
                session.setAttribute("userId", rs.getInt("Id"));
                session.setAttribute("userName", rs.getString("Name"));
                session.setAttribute("userRole", role);
                // Redirect theo role
                switch (role) {
                    case "Admin":
                        response.sendRedirect("index.html");
                        break;
                    case "Customer":
                        response.sendRedirect("index.jsp");
                        break;
                    case "Trainer":
                        response.sendRedirect("trainer-dashboard.jsp");
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
                userDao.addUserFromGoogle(googleAcc); // thêm user mới (Customer mặc định)
            }

            User user = userDao.getUserByEmail(googleAcc.getEmail()); // lấy thông tin đầy đủ từ DB
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
            session.setAttribute("userId", user.getId());
            session.setAttribute("userName", user.getUserName());
            session.setAttribute("userRole", user.getRole());

            // Redirect theo role
            switch (user.getRole()) {
                case "Admin":
                    response.sendRedirect("index.html");
                    break;
                case "Customer":
                    response.sendRedirect("index.jsp");
                    break;
                case "Trainer":
                    response.sendRedirect("trainer-dashboard.jsp");
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