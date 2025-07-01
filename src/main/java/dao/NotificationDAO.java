package dao;

import connectDB.ConnectDatabase;
import model.Notification;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
//
//    // Phương thức main để kiểm tra DAO (Tùy chọn)
//    public static void main(String[] args) throws ClassNotFoundException {
//        NotificationDAO notificationDAO = new NotificationDAO();
//
//        // Thử thêm một thông báo mới (ví dụ với UserID = 1)
//        Notification newNoti = new Notification();
//        newNoti.setUserID(1); // Thay bằng ID người dùng thực tế
//        newNoti.setTitle("Thông báo mới từ hệ thống");
//        newNoti.setContent("Bạn có một sự kiện sắp tới vào 10h sáng ngày mai.");
//        newNoti.setCreatedTime(LocalDateTime.now());
//
//        if (notificationDAO.addNotification(newNoti)) {
//            System.out.println("Thêm thông báo thành công.");
//        } else {
//            System.out.println("Thêm thông báo thất bại.");
//        }
//
//        // Thử lấy và hiển thị thông báo cho UserID = 1
//        System.out.println("\n--- Danh sách thông báo của User ID 1 ---");
//        List<Notification> userNotifications = notificationDAO.getNotificationsByUserId(1);
//        if (userNotifications.isEmpty()) {
//            System.out.println("Không có thông báo nào cho người dùng này.");
//        } else {
//            for (Notification noti : userNotifications) {
//                System.out.println("ID: " + noti.getNotificationID() +
//                                   ", Tiêu đề: " + noti.getTitle() +
//                                   ", Nội dung: " + noti.getContent() +
//                                   ", Thời gian: " + noti.getTimeAgo());
//            }
//        }
//    }
//}