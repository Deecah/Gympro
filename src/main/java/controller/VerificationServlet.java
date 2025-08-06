package controller;

import Utils.HashUtil;
import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import model.User;

import java.io.IOException;
import java.util.Properties;
import java.util.Random;

// MailAuthenticator class embedded here
final class MailAuthenticator extends Authenticator {
    private final String user;
    private final String password;

    public MailAuthenticator(String user, String password) {
        this.user = user;
        this.password = password;
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(user, password);
    }
}

@WebServlet(name = "VerificationServlet", urlPatterns = {"/VerificationServlet"})
public class VerificationServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        int code = new Random().nextInt(900000) + 100000;

        HttpSession session = request.getSession();
        session.setAttribute("verificationCode", code);
        session.setAttribute("email", email);
        session.setAttribute("name", name);
        session.setAttribute("password", password);
        session.setAttribute("role", role);

        final String from = "swptest391@gmail.com";
        final String pass = "qnvekkrbltwixoqg";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        MailAuthenticator authenticator = new MailAuthenticator(from, pass);
        Session mailSession = Session.getInstance(props, authenticator);

        try {
            Message message = new MimeMessage(mailSession);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Email Verification Code");
            message.setText("Your verification code is: " + code);
            Transport.send(message);

            response.sendRedirect("verify.jsp");

        } catch (MessagingException e) {
            e.printStackTrace();
            response.getWriter().println("Failed to send email: " + e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int enteredCode;
        try {
            enteredCode = Integer.parseInt(request.getParameter("code"));
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid code format.");
            request.getRequestDispatcher("verify.jsp").forward(request, response);
            return;
        }

        HttpSession session = request.getSession();
        Integer savedCode = (Integer) session.getAttribute("verificationCode");
        String email = (String) session.getAttribute("email");
        String name = (String) session.getAttribute("name");
        String password = (String) session.getAttribute("password");
        String role = (String) session.getAttribute("role");

        if (savedCode == null || email == null) {
            response.getWriter().println("Session expired. Please try again.");
            return;
        }

        if (enteredCode == savedCode) {
            byte[] hashedPassword = HashUtil.hashPassword(password);
            User user = new User(name, email, hashedPassword, role);
            UserDAO userDAO = new UserDAO();
            userDAO.addUser(user);
            session.invalidate();
            response.sendRedirect("login.jsp?msg=success");
        } else {
            response.sendRedirect("login.jsp?msg=fail");
        }
    }
}
