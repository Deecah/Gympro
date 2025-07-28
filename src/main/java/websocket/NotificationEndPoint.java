
package websocket;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@jakarta.websocket.server.ServerEndpoint("/notification")
public class NotificationEndPoint {

     // Danh sách session đang kết nối tới endpoint
    private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<>());
    
    // Map để lưu trữ userId và session tương ứng
    private static final Map<Integer, Session> userSessions = Collections.synchronizedMap(new HashMap<>());

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
        System.out.println("Client connect: " + session.getId());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        // Handle user registration for notifications
        try {
            if (message.startsWith("register:")) {
                int userId = Integer.parseInt(message.substring(9));
                userSessions.put(userId, session);
                System.out.println("User " + userId + " registered for notifications");
            }
        } catch (Exception e) {
            System.out.println("Error parsing user registration: " + e.getMessage());
        }
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
        // Remove user from userSessions map
        userSessions.entrySet().removeIf(entry -> entry.getValue().equals(session));
        System.out.println("Client disconnect: " + session.getId());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        sessions.remove(session);
        // Remove user from userSessions map
        userSessions.entrySet().removeIf(entry -> entry.getValue().equals(session));
        System.out.println("Error at client " + session.getId() + ": " + throwable.getMessage());
    }

    // Gửi thông báo đến tất cả client đang kết nối
    public static void broadcast(String message) {
        System.out.println("Broadcasting message: " + message);
        synchronized (sessions) {
            for (Session session : sessions) {
                if (session.isOpen()) {
                    try {
                        session.getBasicRemote().sendText(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    
    // Gửi thông báo đến user cụ thể
    public static void sendToUser(int userId, String message) {
        System.out.println("Sending message to user " + userId + ": " + message);
        synchronized (userSessions) {
            Session session = userSessions.get(userId);
            if (session != null && session.isOpen()) {
                try {
                    session.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
