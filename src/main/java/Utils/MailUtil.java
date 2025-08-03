package Utils;

import constant.EmailInfor;
import controller.password.ResetPasswordServlet;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MailUtil {
    private static final Logger LOGGER = Logger.getLogger(MailUtil.class.getName());
    private final class MailAuthenticator extends jakarta.mail.Authenticator {
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
    
    public void sendEmail(HttpServletRequest request, HttpServletResponse response, String email, String subject, String content){
         try {
            String fromEmail = EmailInfor.EMAIL;
            String appPassword = EmailInfor.APP_PASSWORD; 

            Properties props = new Properties();
            props.put("mail.smtp.host", EmailInfor.HOST);
            props.put("mail.smtp.port", EmailInfor.PORT);
            props.put("mail.smtp.auth", EmailInfor.AUTH);
            props.put("mail.smtp.starttls.enable",EmailInfor.STARTTLS_ENABLE); // Enable TLS
            props.put("mail.debug", "true"); // Rất hữu ích cho việc debug

            // Sử dụng lớp MailAuthenticator đã định nghĩa
            Session mailSession = Session.getInstance(props, new MailAuthenticator(fromEmail, appPassword));
            
            Message message = new MimeMessage(mailSession);
            message.setFrom(new InternetAddress(fromEmail)); // Sử dụng fromEmail
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Your verify code to reset password");
            message.setContent(content, "text/html; charset=UTF-8"); // setContent(Object obj, String type)
            // obj là content chứa đoạn nội dung
            // type có thể là "text/plain", "text/html", ... Tùy thuộc vào định dạng
            
            Transport.send(message);
            HttpSession session = request.getSession();                 
            session.setAttribute("email", email);
            request.setAttribute("mess", "An email has been sent to you. Please check your mail box!!!");
        } catch (MessagingException e) {
            LOGGER.log(Level.SEVERE, "Failed to send email", e);
            request.setAttribute("mess", "Cannot send mail. Please try again later!!");
        }
    }
}
