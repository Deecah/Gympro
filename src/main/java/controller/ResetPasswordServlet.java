package controller;

import Utils.HashUtil;
import dao.UserDAO;
import dao.UserTokenDAO;
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
import jakarta.servlet.http.HttpSession;
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
    private static final Logger LOGGER = Logger.getLogger(ResetPasswordServlet.class.getName());

    private static class TokenGenerator {

        public static String generateShortToken() {
            return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 8);
        }
    }

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
        request.getRequestDispatcher("requestPassword.jsp").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if (null == action) {
                resetPassword(request, response);
            } else {
                switch (action) {
                    case "request":
                        requestPassword(request, response);
                        break;
                    case "confirm":
                        confirmToken(request, response);
                        break;
                    default:
                        resetPassword(request, response);
                        break;
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

    private void requestPassword(HttpServletRequest request, HttpServletResponse response) throws IOException, ClassNotFoundException, SQLException, ServletException {
        String email = request.getParameter("email");
        String token = TokenGenerator.generateShortToken();

        if (email == null || email.isEmpty()) {
            response.getWriter().println("Invalid email. Please try again.");
            return;
        } else {
            UserDAO userDAO = new UserDAO();
            User u = userDAO.getUserByEmail(email);
            if (u == null) {
                response.getWriter().println("Your email is not exist. Please try again.");
            }
            UserTokenDAO tokenDAO = new UserTokenDAO();
            UserToken userToken = new UserToken(u.getId(), token, "password_reset", expireDateTime(), false, LocalDateTime.now());
            tokenDAO.addUserToken(userToken);

            //send email
            try {
                String from = "swptest391@gmail.com";
                String pass = "qnvekkrbltwixoqg";
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
                message.setSubject("Your verify code to reset password");
                message.setContent("<h1>" + token + "</h1>", "text/html; charset=UTF-8");
                Transport.send(message);
                HttpSession session = request.getSession();
                session.setAttribute("email", email);
                request.setAttribute("mess", "An email is sent to you. Please check your inbox.");
                request.getRequestDispatcher("verifyResetToken.jsp").forward(request, response);     
            } catch (MessagingException e) {
                Logger.getLogger(ResetPasswordServlet.class.getName()).log(Level.SEVERE, "Failed to send email", e);
                request.setAttribute("mess", "Failed to send email. Please try again later.");
                request.getRequestDispatcher("requestPassword.jsp").forward(request, response);
            }
        }

    }

    private void confirmToken(HttpServletRequest request, HttpServletResponse response) throws ClassNotFoundException, ServletException, IOException, SQLException {
        String token = request.getParameter("token");
        UserTokenDAO tokenDAO = new UserTokenDAO();
        UserToken userToken = tokenDAO.getUserTokenbyToken(token);
        if (userToken == null) {
            request.setAttribute("mess", "Please enter verify code!!!");
            request.getRequestDispatcher("verifyResetToken.jsp").forward(request, response);
        }
        if (isExpiredTime(userToken.getExpiry())) {
            request.setAttribute("mess", "Your verify code is expired!!! Re-enter your gmail to get new code");
            request.getRequestDispatcher("requestPassword.jsp").forward(request, response);
        }
        if (userToken.isUsed()) {
            request.setAttribute("mess", "Your verify code is used!!! Re-enter your gmail to get new code");
            request.getRequestDispatcher("requestPassword.jsp").forward(request, response);
        }
        if (token.equals(userToken.getToken())) {
            userToken.setUsed(true);
            tokenDAO.updateUserToken(userToken);
            request.getRequestDispatcher("resetPassword.jsp").forward(request, response);
        }

    }

    public LocalDateTime expireDateTime() {
        return LocalDateTime.now().plusMinutes(EXPIRY_TIME);
    }

    public boolean isExpiredTime(LocalDateTime time) {
        return LocalDateTime.now().isAfter(time);
    }

    private void resetPassword(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ClassNotFoundException, SQLException { // Thêm ServletException, IOException
        String password1 = request.getParameter("password");
        String password2 = request.getParameter("confirmPassword");
        if (password1 == null || password2 == null || password1.isEmpty() || password2.isEmpty() || !password1.equals(password2)) {
            request.setAttribute("mess", "Password is not match or missing!!!");
            request.getRequestDispatcher("resetPassword.jsp").forward(request, response);
        }
        UserDAO userDAO = new UserDAO();
        HttpSession session = request.getSession();
        String email = (String) session.getAttribute("email");
        User u = userDAO.getUserByEmail(email);
        byte[] newPasswordHashed = HashUtil.hashPassword(password1); // Băm mật khẩu mới
        userDAO.updatePassword(u.getId(), newPasswordHashed); // Cập nhật mật khẩu trong Users
        request.setAttribute("mess", "Reset password successfully!!!");
        request.getRequestDispatcher("login.jsp").forward(request, response);

    }
}
