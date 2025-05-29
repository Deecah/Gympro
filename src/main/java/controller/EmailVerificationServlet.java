package controller;

import java.io.IOException;
import java.util.Properties;
import java.util.Random;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

public class EmailVerificationServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("signup".equals(action)) { // Ensures correct action
            sendVerificationEmail(request, response);
        } else if ("verify".equals(action)) {
            verifyCode(request, response);
        }
    }

    private void sendVerificationEmail(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String email = request.getParameter("email");

        if (email == null || email.isEmpty()) {
            response.getWriter().println("Invalid email. Please try again.");
            return;
        }

        int code = new Random().nextInt(900000) + 100000; // Generate 6-digit code
        HttpSession session = request.getSession();
        session.setAttribute("verificationCode", code);
        session.setAttribute("email", email);

        try {
            String host = "smtp.gmail.com"; // Replace with actual SMTP server
            String port = "587";
            String from = "swptest391@gmail.com"; // Your sending email
            String pass = "qnvekkrbltwixoqg"; // App Password for email authentication

            Properties props = new Properties();
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true"); // Enable TLS

            Session mailSession = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(from, pass);
                }
            });

            mailSession.setDebug(true); // Enable debugging

            Message message = new MimeMessage(mailSession);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Your Email Verification Code");
            message.setText("Your verification code is: " + code);
            Transport.send(message);

            response.sendRedirect("verify.jsp"); // Redirect to verification page

        } catch (MessagingException e) {
            e.printStackTrace();
            response.getWriter().println("Failed to send email: " + e.getMessage());
        }
    }

    private void verifyCode(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int enteredCode;
        try {
enteredCode = Integer.parseInt(request.getParameter("code"));
        } catch (NumberFormatException e) {
            response.getWriter().println("Invalid code format.");
            return;
        }

        HttpSession session = request.getSession();
        Integer savedCode = (Integer) session.getAttribute("verificationCode");
        String email = (String) session.getAttribute("email");

        if (savedCode == null || email == null) {
            response.getWriter().println("Session expired. Please try again.");
            return;
        }

        if (enteredCode == savedCode) {
            response.getWriter().println("Verification successful! Account activated for: " + email);
            session.invalidate(); // Clear session data
        } else {
            response.getWriter().println("Invalid verification code. Please try again.");
        }
    }
}