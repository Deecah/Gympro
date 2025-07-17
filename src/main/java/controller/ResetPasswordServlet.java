package controller;

import Utils.HashUtil;
import dao.UserDAO;
import dao.UserTokenDAO;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
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

    final class MailAuthenticator extends jakarta.mail.Authenticator {

        private final String user;
        private final String password;

        public MailAuthenticator(String user, String password) {
            this.user = user;
            this.password = password;
        }

        @Override
        protected jakarta.mail.PasswordAuthentication getPasswordAuthentication() {
            return new jakarta.mail.PasswordAuthentication(user, password);
        }
    }

    private static class TokenGenerator {

        public static String generateShortToken() {
            return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 8);
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
            if (action == null) {
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
            request.setAttribute("errorMessage", "A system error occurred. Please try again later."); // Translated
            request.getRequestDispatcher("errorPage.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Reset Password Servlet handles password reset requests"; // Translated
    }

    private void requestPassword(HttpServletRequest request, HttpServletResponse response) throws IOException, ClassNotFoundException, SQLException, ServletException {
        String email = request.getParameter("email");
        String token = TokenGenerator.generateShortToken();

        if (email == null || email.isEmpty()) {
            request.setAttribute("mess", "Invalid email. Please try again."); // Translated
            request.getRequestDispatcher("requestPassword.jsp").forward(request, response);
            return;
        }

        UserDAO userDAO = new UserDAO();
        User u = userDAO.getUserByEmail(email);
        if (u == null) {
            request.setAttribute("mess", "Your email does not exist. Please try again."); // Translated
            request.getRequestDispatcher("requestPassword.jsp").forward(request, response);
            return;
        }

        UserTokenDAO tokenDAO = new UserTokenDAO();
        UserToken userToken = new UserToken(u.getUserId(), token, "password_reset", expireDateTime(), false, LocalDateTime.now());
        tokenDAO.addUserToken(userToken);

        //send email
        try {
            String fromEmail = "swptest391@gmail.com";
            String appPassword = "yggqrlbuinwhpkny";

            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.debug", "true");

            Session mailSession = Session.getInstance(props, new MailAuthenticator(fromEmail, appPassword));

            mailSession.setDebug(true);
            Message message = new MimeMessage(mailSession);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Your verify code to reset password");
            message.setContent("<h1>" + token + "</h1>", "text/html; charset=UTF-8");
            Transport.send(message);

            HttpSession session = request.getSession();
            session.setAttribute("email", email);
            request.setAttribute("mess", "An email has been sent to you. Please check your inbox."); // Translated
            request.getRequestDispatcher("verifyResetToken.jsp").forward(request, response);
        } catch (MessagingException e) {
            LOGGER.log(Level.SEVERE, "Failed to send email", e);
            request.setAttribute("mess", "Failed to send email. Please try again later."); // Translated
            request.getRequestDispatcher("requestPassword.jsp").forward(request, response);
        }
    }

    private void confirmToken(HttpServletRequest request, HttpServletResponse response) throws ClassNotFoundException, ServletException, IOException, SQLException {
        String token = request.getParameter("token");
        UserTokenDAO tokenDAO = new UserTokenDAO();
        UserToken userToken = tokenDAO.getUserTokenbyToken(token);

        if (userToken == null) {
            request.setAttribute("mess", "Please enter the verification code!"); // Translated
            request.getRequestDispatcher("verifyResetToken.jsp").forward(request, response);
            return;
        }

        if (isExpiredTime(userToken.getExpiry())) {
            request.setAttribute("mess", "Your verification code has expired! Please re-enter your email to receive a new code."); // Translated
            request.getRequestDispatcher("requestPassword.jsp").forward(request, response);
            return;
        }

        if (userToken.isUsed()) {
            request.setAttribute("mess", "Your verification code has already been used! Please re-enter your email to receive a new code."); // Translated
            request.getRequestDispatcher("requestPassword.jsp").forward(request, response);
            return;
        }

        if (token.equals(userToken.getToken())) {
            userToken.setUsed(true);
            tokenDAO.updateUserToken(userToken);
            request.getRequestDispatcher("resetPassword.jsp").forward(request, response);
        } else {
             request.setAttribute("mess", "Incorrect verification code. Please check again."); // Translated
             request.getRequestDispatcher("verifyResetToken.jsp").forward(request, response);
        }
    }

    public LocalDateTime expireDateTime() {
        return LocalDateTime.now().plusMinutes(EXPIRY_TIME);
    }

    public boolean isExpiredTime(LocalDateTime time) {
        return LocalDateTime.now().isAfter(time);
    }

    private void resetPassword(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ClassNotFoundException, SQLException {
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        if (password == null || confirmPassword == null || password.isEmpty() || confirmPassword.isEmpty() || !password.equals(confirmPassword)) {
            request.setAttribute("mess", "Passwords do not match or are missing!"); // Translated
            request.getRequestDispatcher("resetPassword.jsp").forward(request, response);
            return;
        }

        UserDAO userDAO = new UserDAO();
        HttpSession session = request.getSession();
        String email = (String) session.getAttribute("email");

        if (email == null || email.isEmpty()) {
            request.setAttribute("mess", "Session expired or invalid. Please restart the password reset process."); // Translated
            request.getRequestDispatcher("requestPassword.jsp").forward(request, response);
            return;
        }

        User u = userDAO.getUserByEmail(email);
        if (u == null) {
            request.setAttribute("mess", "User does not exist. Please restart the password reset process."); // Translated
            request.getRequestDispatcher("requestPassword.jsp").forward(request, response);
            return;
        }

        byte[] newPasswordHashed = HashUtil.hashPassword(password);
        userDAO.updatePassword(u.getUserId(), newPasswordHashed);

        request.setAttribute("mess", "Password reset successful!"); // Translated
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }
}
