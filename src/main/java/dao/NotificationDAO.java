package dao;

import connectDB.ConnectDatabase;
import controller.NotificationServlet;
import model.Notification;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.User;
import org.apache.http.HttpRequest;

public class NotificationDAO {

    private static final Logger LOGGER = Logger.getLogger(NotificationDAO.class.getName());
    
    public List<Notification> getNotificationsByUserId(int userId) throws ClassNotFoundException {
        List<Notification> notifications = new ArrayList<>();
        String sql = "SELECT * FROM [dbo].[Notification] " +
                     "WHERE UserID = ? " +
                     "ORDER BY CreatedTime DESC";

        ConnectDatabase db = ConnectDatabase.getInstance();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = db.openConnection(); // Mở kết nối
            ps = con.prepareStatement(sql); // Chuẩn bị câu lệnh SQL
            ps.setInt(1, userId); // Đặt giá trị cho tham số UserID
            rs = ps.executeQuery(); // Thực thi truy vấn

            while (rs.next()) { // Duyệt qua các hàng kết quả
                Notification notification = new Notification();
                notification.setNotificationID(rs.getInt("NotificationID"));
                notification.setUserID(rs.getInt("UserID"));
                notification.setTitle(rs.getString("NotificationTitle"));
                notification.setContent(rs.getString("NotificationContent"));

                // Chuyển đổi Timestamp từ DB sang LocalDateTime
                Timestamp createdTimestamp = rs.getTimestamp("CreatedTime");
                if (createdTimestamp != null) {
                    notification.setCreatedTime(createdTimestamp.toLocalDateTime());
                }
                notifications.add(notification); // Thêm thông báo vào danh sách
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi lấy thông báo cho UserID: " + userId, e);
        } finally {
            // Đảm bảo đóng tất cả tài nguyên trong khối finally
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "Lỗi khi đóng tài nguyên trong getNotificationsByUserId", ex);
            }
        }
        return notifications;
    }

    public boolean addNotification(int userId, String title, String content) throws ClassNotFoundException {
        // SQL INSERT chỉ bao gồm UserID, Title, Content
        // CreatedTime sẽ được set tự động bởi DEFAULT GETDATE() trong database
        String sql = "INSERT INTO [dbo].[Notification] (UserID, NotificationTitle, NotificationContent) " +
                     "VALUES (?, ?, ?)";

        ConnectDatabase db = ConnectDatabase.getInstance();
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = db.openConnection(); // Mở kết nối
            ps = con.prepareStatement(sql); // Chuẩn bị câu lệnh SQL

            // Đặt giá trị cho các tham số
            ps.setInt(1, userId);
            ps.setString(2, title);
            ps.setString(3, content);
            
            // Không cần set CreatedTime vì database sẽ tự động set GETDATE()

            int rowsAffected = ps.executeUpdate(); // Thực thi câu lệnh INSERT
            return rowsAffected > 0; // Trả về true nếu có hàng nào được thêm
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi thêm thông báo: " + title, e);
            return false;
        } finally {
            // Đảm bảo đóng tất cả tài nguyên trong khối finally
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "Lỗi khi đóng tài nguyên trong addNotification", ex);
            }
        }
    }
    
 
    public void addNotificationToAllUsers(String title, String content) throws ClassNotFoundException {
        UserDAO userDAO = new UserDAO();
        ArrayList<User> allUsers = userDAO.getAllUsers();  
        for (User user : allUsers) {
            try {
                boolean success = addNotification(user.getUserId(), title, content);
                if (success) {
                    NotificationServlet sendPopUp = new NotificationServlet();
                    sendPopUp.sendPopupNotification("🔔 New Notification");
                    LOGGER.info("Đã gửi thông báo thành công cho user ID: " + user.getUserId());
                } else {
                    LOGGER.warning("Không thể gửi thông báo cho user ID: " + user.getUserId());
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Lỗi khi gửi thông báo cho user ID: " + user.getUserId(), e);
            }
        }
        LOGGER.info("Hoàn thành gửi thông báo. Tổng: " + allUsers.size() + " users, Thành công: ");
    }
    
     public boolean addNotification(Notification notification) throws ClassNotFoundException {
        // SQL INSERT bao gồm UserID, Title, Content và CreatedTime
        // Lưu ý: Nếu cột CreatedTime trong DB có DEFAULT (getdate()), bạn có thể bỏ qua nó trong câu SQL và setTimestamp
        String sql = "INSERT INTO [dbo].[Notification] (UserID, NotificationTitle, NotificationContent, CreatedTime) " +
                     "VALUES (?, ?, ?, ?)";

        ConnectDatabase db = ConnectDatabase.getInstance();
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = db.openConnection(); // Mở kết nối
            ps = con.prepareStatement(sql); // Chuẩn bị câu lệnh SQL

            // Đặt giá trị cho các tham số
            ps.setInt(1, notification.getUserID());
            ps.setString(2, notification.getTitle());
            ps.setString(3, notification.getContent());
            
            // Chuyển đổi LocalDateTime sang Timestamp để lưu vào DB
            ps.setTimestamp(4, Timestamp.valueOf(notification.getCreatedTime()));

            int rowsAffected = ps.executeUpdate(); // Thực thi câu lệnh UPDATE/INSERT/DELETE
            return rowsAffected > 0; // Trả về true nếu có hàng nào bị ảnh hưởng
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi thêm thông báo: " + notification.getTitle(), e);
            return false;
        } finally {
            // Đảm bảo đóng tất cả tài nguyên trong khối finally
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "Lỗi khi đóng tài nguyên trong addNotification", ex);
            }
        }
    }

}