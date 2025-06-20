
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
import controller.GoogleLogin;

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
                        response.sendRedirect("trainer-dashboard.jsp");
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
            session.setAttribute("userId", user.getUserId());
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