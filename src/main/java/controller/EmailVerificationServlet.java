package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Random;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import connectDB.ConnectDatabase;

public class EmailVerificationServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("signup".equals(action)) {
            sendVerificationEmail(request, response);
        } else if ("verify".equals(action)) {
            verifyCodeAndRegisterUser(request, response);
        }
    }

    private void sendVerificationEmail(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String email = request.getParameter("email");
        String name = request.getParameter("name");
        String password = request.getParameter("password"); // Plain text for now
        String role = request.getParameter("role"); // Customer or Trainer

        if (email == null || email.isEmpty()) {
            response.getWriter().println("Invalid email. Please try again.");
            return;
        }

        int code = new Random().nextInt(900000) + 100000; // Generate 6-digit code
        HttpSession session = request.getSession();
        session.setAttribute("verificationCode", code);
        session.setAttribute("email", email);
        session.setAttribute("name", name);
        session.setAttribute("password", password);
        session.setAttribute("role", role);

        try {
            String host = "smtp.gmail.com"; // Outlook SMTP
            String from = "swptest391@gmail.com"; // Your sender email
            String pass = "qnvekkrbltwixoqg"; // Use an App Password if MFA is enabled

            Properties props = new Properties();
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");

            Session mailSession = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(from, pass);
                }
            });

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

    private void verifyCodeAndRegisterUser(HttpServletRequest request, HttpServletResponse response)
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
        String name = (String) session.getAttribute("name");
        String password = (String) session.getAttribute("password");
        String role = (String) session.getAttribute("role");

        if (savedCode == null || email == null) {
            response.getWriter().println("Session expired. Please try again.");
            return;
        }

        if (enteredCode == savedCode) {
            response.getWriter().println("Verification successful! Account activated for: " + email);
            storeUserInDatabase(name, email, password, role); // Store user data
            session.invalidate(); // Clear session data
        } else {
            response.getWriter().println("Invalid verification code. Please try again.");
        }
    }

    private void storeUserInDatabase(String name, String email, String password, String role) {
        try (Connection con = ConnectDatabase.getInstance().openConnection()) {
            if (con != null) {
                String sql = "INSERT INTO Users (Name, Email, Password, Role) VALUES (?, ?, HASHBYTES('SHA2_256', ?), ?)";
                try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                    pstmt.setString(1, name);
                    pstmt.setString(2, email);
                    pstmt.setString(3, password); // Secure hash applied
                    pstmt.setString(4, role);
                    pstmt.executeUpdate();
                    System.out.println("User successfully stored in the database.");
                }
            } else {
                System.out.println("Failed to connect to the database.");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

