package Utils;

import dao.NotificationDAO;
import model.Notification;
import websocket.NotificationEndPoint;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.time.LocalDateTime;

public class NotificationUtil {
    
    private static final Gson gson = new Gson();
    
    /**
     * Gửi notification real-time và lưu vào database
     * @param userId ID của user nhận notification
     * @param title Tiêu đề notification
     * @param content Nội dung notification
     * @param type Loại notification (success, error, info, warning)
     */
    public static void sendNotification(int userId, String title, String content, String type) {
        try {
            // Lưu notification vào database
            Notification notification = new Notification();
            notification.setUserID(userId);
            notification.setTitle(title);
            notification.setContent(content);
            notification.setCreatedTime(LocalDateTime.now());
            
            NotificationDAO notificationDAO = new NotificationDAO();
            notificationDAO.addNotification(notification);
            
            // Gửi notification real-time qua WebSocket
            JsonObject notificationJson = new JsonObject();
            notificationJson.addProperty("type", "notification");
            notificationJson.addProperty("notificationType", type);
            notificationJson.addProperty("title", title);
            notificationJson.addProperty("content", content);
            notificationJson.addProperty("userId", userId);
            notificationJson.addProperty("timestamp", LocalDateTime.now().toString());
            
            String message = gson.toJson(notificationJson);
            NotificationEndPoint.sendToUser(userId, message);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Gửi notification thành công
     */
    public static void sendSuccessNotification(int userId, String title, String content) {
        sendNotification(userId, title, content, "success");
    }
    
    /**
     * Gửi notification lỗi
     */
    public static void sendErrorNotification(int userId, String title, String content) {
        sendNotification(userId, title, content, "error");
    }
    
    /**
     * Gửi notification thông tin
     */
    public static void sendInfoNotification(int userId, String title, String content) {
        sendNotification(userId, title, content, "info");
    }
    
    /**
     * Gửi notification cảnh báo
     */
    public static void sendWarningNotification(int userId, String title, String content) {
        sendNotification(userId, title, content, "warning");
    }
} 