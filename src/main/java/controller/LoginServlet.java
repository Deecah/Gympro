
package controller;

import Utils.HashUtil;
import connectDB.ConnectDatabase;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
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
        String hashedPassword = HashUtil.hashPassword(password);
        try (Connection conn = ConnectDatabase.getInstance().openConnection()) {
            String sql = "INSERT INTO Users (Name, Email, Password, Role, Status) VALUES (?, ?, ?, ?, 'Normal')";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, hashedPassword);
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
        String hashedPassword = HashUtil.hashPassword(password);
        try (Connection conn = ConnectDatabase.getInstance().openConnection()) {
            String sql = "SELECT * FROM Users WHERE Email = ? AND Password = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, hashedPassword);
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
                        response.sendRedirect("admin-dashboard.jsp");
                        break;
                    case "Customer":
                        response.sendRedirect("customer-home.jsp");
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

}
