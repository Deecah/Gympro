package controller;

import Utils.HashUtil;
import dao.UserDAO;
import dao.UserTokenDAO;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
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

    // Class MailAuthenticator đã được định nghĩa bên ngoài hoặc bạn có thể định nghĩa lại ở đây
    // Nếu bạn muốn dùng lại MailAuthenticator đã sửa ở câu trước, hãy đặt nó ở một file riêng
    // hoặc ngay trong file này (nhưng không phải là inner class)
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
            if (action == null) { // Đổi từ null == action sang action == null để dễ đọc hơn
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
            // Có thể thêm forward đến trang lỗi hoặc hiển thị thông báo lỗi thân thiện hơn
            request.setAttribute("errorMessage", "Đã xảy ra lỗi hệ thống. Vui lòng thử lại sau.");
            request.getRequestDispatcher("errorPage.jsp").forward(request, response);
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
            request.setAttribute("mess", "Email không hợp lệ. Vui lòng thử lại.");
            request.getRequestDispatcher("requestPassword.jsp").forward(request, response);
            return;
        }

        UserDAO userDAO = new UserDAO();
        User u = userDAO.getUserByEmail(email);
        if (u == null) {
            request.setAttribute("mess", "Email của bạn không tồn tại. Vui lòng thử lại.");
            request.getRequestDispatcher("requestPassword.jsp").forward(request, response);
            return; // Thêm return để dừng xử lý nếu email không tồn tại
        }

        UserTokenDAO tokenDAO = new UserTokenDAO();
        UserToken userToken = new UserToken(u.getUserId(), token, "password_reset", expireDateTime(), false, LocalDateTime.now());
        tokenDAO.addUserToken(userToken);

        //send email
        try {
            String fromEmail = "swptest391@gmail.com";
            // Sử dụng mật khẩu ứng dụng (App Password) thay vì mật khẩu thông thường của Gmail
            String appPassword = "yggqrlbuinwhpkny"; 

            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true"); // Enable TLS
            props.put("mail.debug", "true"); // Rất hữu ích cho việc debug

            // Sử dụng lớp MailAuthenticator đã định nghĩa
            Session mailSession = Session.getInstance(props, new MailAuthenticator(fromEmail, appPassword));
            
            mailSession.setDebug(true); // Đã có ở trên, có thể bỏ dòng này
            Message message = new MimeMessage(mailSession);
            message.setFrom(new InternetAddress(fromEmail)); // Sử dụng fromEmail
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Your verify code to reset password");
            message.setContent("<h1>" + token + "</h1>", "text/html; charset=UTF-8");
            Transport.send(message);

            HttpSession session = request.getSession();                 
            session.setAttribute("email", email);
            request.setAttribute("mess", "Một email đã được gửi đến bạn. Vui lòng kiểm tra hộp thư đến.");
            request.getRequestDispatcher("verifyResetToken.jsp").forward(request, response);
        } catch (MessagingException e) {
            LOGGER.log(Level.SEVERE, "Failed to send email", e);
            request.setAttribute("mess", "Không thể gửi email. Vui lòng thử lại sau.");
            request.getRequestDispatcher("requestPassword.jsp").forward(request, response);
        }
    }

    private void confirmToken(HttpServletRequest request, HttpServletResponse response) throws ClassNotFoundException, ServletException, IOException, SQLException {
        String token = request.getParameter("token");
        UserTokenDAO tokenDAO = new UserTokenDAO();
        UserToken userToken = tokenDAO.getUserTokenbyToken(token);

        if (userToken == null) {
            request.setAttribute("mess", "Vui lòng nhập mã xác minh!!!");
            request.getRequestDispatcher("verifyResetToken.jsp").forward(request, response);
            return; // Thêm return
        }

        if (isExpiredTime(userToken.getExpiry())) {
            request.setAttribute("mess", "Mã xác minh của bạn đã hết hạn!!! Vui lòng nhập lại gmail để nhận mã mới.");
            request.getRequestDispatcher("requestPassword.jsp").forward(request, response);
            return; // Thêm return
        }

        if (userToken.isUsed()) {
            request.setAttribute("mess", "Mã xác minh của bạn đã được sử dụng!!! Vui lòng nhập lại gmail để nhận mã mới.");
            request.getRequestDispatcher("requestPassword.jsp").forward(request, response);
            return; // Thêm return
        }

        if (token.equals(userToken.getToken())) {
            userToken.setUsed(true);
            tokenDAO.updateUserToken(userToken);
            request.getRequestDispatcher("resetPassword.jsp").forward(request, response);
        } else {
             request.setAttribute("mess", "Mã xác minh không đúng. Vui lòng kiểm tra lại.");
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
        String password = request.getParameter("password"); // Đổi password1 thành password
        String confirmPassword = request.getParameter("confirmPassword"); // Đổi password2 thành confirmPassword

        if (password == null || confirmPassword == null || password.isEmpty() || confirmPassword.isEmpty() || !password.equals(confirmPassword)) {
            request.setAttribute("mess", "Mật khẩu không khớp hoặc bị thiếu!!!");
            request.getRequestDispatcher("resetPassword.jsp").forward(request, response);
            return; // Thêm return
        }

        UserDAO userDAO = new UserDAO();
        HttpSession session = request.getSession();
        String email = (String) session.getAttribute("email");

        if (email == null || email.isEmpty()) {
            // Trường hợp không có email trong session (ví dụ: người dùng truy cập trực tiếp)
            request.setAttribute("mess", "Phiên làm việc hết hạn hoặc không hợp lệ. Vui lòng bắt đầu lại quá trình đặt lại mật khẩu.");
            request.getRequestDispatcher("requestPassword.jsp").forward(request, response);
            return;
        }

        User u = userDAO.getUserByEmail(email);
        if (u == null) {
            // Trường hợp email trong session không tìm thấy trong DB (hiếm khi xảy ra nếu logic đúng)
            request.setAttribute("mess", "Người dùng không tồn tại. Vui lòng bắt đầu lại quá trình đặt lại mật khẩu.");
            request.getRequestDispatcher("requestPassword.jsp").forward(request, response);
            return;
        }

        byte[] newPasswordHashed = HashUtil.hashPassword(password); // Băm mật khẩu mới
        userDAO.updatePassword(u.getUserId(), newPasswordHashed); // Cập nhật mật khẩu trong Users

        request.setAttribute("mess", "Đặt lại mật khẩu thành công!!!");
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }
}