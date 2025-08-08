//package websocket;
//
//import jakarta.websocket.*;
//import jakarta.websocket.server.ServerEndpoint;
//import com.google.gson.Gson;
//import dao.CommentDAO;
//import model.Comment;
//
//import java.io.IOException;
//import java.util.Set;
//import java.util.concurrent.ConcurrentHashMap;
//
//@ServerEndpoint("/ws/comment")
//public class CommentEndpoint {
//    private static Set<Session> sessions = ConcurrentHashMap.newKeySet();
//    private static Gson gson = new Gson();
//
//    @OnOpen
//    public void onOpen(Session session) {
//        sessions.add(session);
//    }
//
//    @OnClose
//    public void onClose(Session session) {
//        sessions.remove(session);
//    }
//
//    @OnMessage
//    public void onMessage(String message, Session session) throws IOException {
//        // Parse JSON message
//        Comment comment = gson.fromJson(message, Comment.class);
//
//        // Lưu comment vào DB
//        CommentDAO commentDAO = new CommentDAO();
//        commentDAO.insertComment(comment);
//
//        // Lấy lại comment vừa lưu (có id, thời gian, userName...)
//        Comment saved = commentDAO.getLastCommentByUserAndWorkout(comment.getUserId(), comment.getWorkoutId());
//
//        // Broadcast cho tất cả client
//        String json = gson.toJson(saved);
//        for (Session s : sessions) {
//            s.getAsyncRemote().sendText(json);
//        }
//    }
//} 