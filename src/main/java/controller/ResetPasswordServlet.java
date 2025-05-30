package controller;

import DAO.UserDAO;
import DAO.UserTokenDAO;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.User;
import model.UserToken;

public class ResetPasswordServlet extends HttpServlet {

    private final int EXPIRY_TIME = 10;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet UserServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet UserServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String token = request.getParameter("token");
        if (token != null && !token.isEmpty()) {
            request.setAttribute("token", token); // Đặt token vào request attribute
            request.getRequestDispatcher("resetPassword.jsp").forward(request, response); // Forward đến JSP
        } else {
            request.getRequestDispatcher("requestPassword.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("request".equals(action)) {
            try {
                requestPassword(request, response);
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(ResetPasswordServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                resetPassword(request, response);
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(ResetPasswordServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

    private void requestPassword(HttpServletRequest request, HttpServletResponse response) throws IOException, ClassNotFoundException, SQLException, ServletException {
        String email = request.getParameter("email");
        String token = TokenGenerator.generateShortToken();
        String link = "http://localhost:8080/SWP391/ResetPasswordServlet?token=" + token;

        if (email == null || email.isEmpty()) {
            response.getWriter().println("Invalid email. Please try again.");
            return;
        } else {
            UserDAO userDAO = new UserDAO();
            User u = userDAO.getUserByEmail(email);
            if (u == null) {
                response.getWriter().println("Your email is not in the database. Please try again.");
            }
            UserTokenDAO tokenDAO = new UserTokenDAO();
            UserToken userToken = new UserToken(u.getId(), token, "password_reset", expireDateTime(), false, LocalDateTime.now());
            tokenDAO.addUserToken(userToken);

            try {
                String from = "swptest391@gmail.com"; // Your sending email
                String pass = "qnvekkrbltwixoqg"; // App Password for email authentication
                Properties props = new Properties();
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.port", "587");
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true"); // Enable TLS
                Session mailSession = Session.getInstance(props, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(from, pass);
                    }
                });
                mailSession.setDebug(true);
                Message message = new MimeMessage(mailSession);
                message.setFrom(new InternetAddress(from));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
                message.setSubject("Click the link below to reset password");
                message.setContent("<a href=" + link + ">Click here</a></p>", "text/html; charset=UTF-8");
                Transport.send(message);
                request.setAttribute("mess", "An email is sent to you. Please check your inbox."); // Thông báo rõ ràng hơn
                request.getRequestDispatcher("requestPassword.jsp").forward(request, response); // Chuyển hướng về trang form
                request.setAttribute("email", email);

            } catch (MessagingException e) {
                Logger.getLogger(ResetPasswordServlet.class.getName()).log(Level.SEVERE, "Failed to send email", e); // Ghi log chi tiết
                request.setAttribute("mess", "Failed to send email. Please try again later."); // Thông báo lỗi thân thiện
                request.getRequestDispatcher("requestPassword.jsp").forward(request, response); // Chuyển hướng về trang form
            }
        }

    }

    public class TokenGenerator {

        public static String generateShortToken() {
            return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 8);
        }
    }

    public LocalDateTime expireDateTime() {
        return LocalDateTime.now().plusMinutes(EXPIRY_TIME);
    }

    public boolean isExpiredTime(LocalDateTime time) {
        return LocalDateTime.now().isAfter(time);
    }

    private void resetPassword(HttpServletRequest request, HttpServletResponse response) throws ClassNotFoundException, SQLException {
        //HttpSession session = request.getSession();
        String token = request.getParameter("token");
        try {
            UserTokenDAO tokenDAO = new UserTokenDAO();
            UserDAO userDAO = new UserDAO();
           
            UserToken userToken = tokenDAO.getUserTokenbyToken(token);
            User u = userDAO.getUserById(userToken.getUserId());

           
            if (LocalDateTime.now().isAfter(userToken.getExpiry()) || userToken.isUsed() == true) {
                request.setAttribute("mess", "Invalid access!!!!");
                return;
            }
            String password1 = request.getParameter("password");
            String password2 = request.getParameter("confirmPassword");
            if (password1 == null || password2 == null || !password1.equals(password2)) {
                request.setAttribute("error", "Passwords do not match or are missing!!!");
                try {
                    request.getRequestDispatcher("http://localhost:8080/SWP391/ResetPasswordServlet?token=" + token).forward(request, response);
                } catch (ServletException | IOException ex) {
                    Logger.getLogger(ChangePasswordServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
                return;
            }
            try {
                userDAO.updatePassword(u.getId(), password1);
                request.setAttribute("error", "Change Password Successful!!!");
                userToken.setUsed(true);
            } catch (SQLException ex) {
                Logger.getLogger(ChangePasswordServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException e) {
        }

    }

}
