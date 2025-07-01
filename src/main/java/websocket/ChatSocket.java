package websocket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import dao.ChatDAO;
import model.Message;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@ServerEndpoint(value = "/ws/chat/{chatId}/{userId}")
public class ChatSocket {


    private static final Map<Integer, Session> SESSIONS = new ConcurrentHashMap<>();
    private static final Map<Session, Integer> USER_IDS = new ConcurrentHashMap<>();

    private static final Gson gson = new GsonBuilder()
    .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) 
        (src, typeOfSrc, context) -> new JsonPrimitive(src.toString()))
    .create();

    // ------------- life-cycle -----------------

    @OnOpen
    public void onOpen(Session session,
                       @PathParam("chatId") int chatId,
                       @PathParam("userId") int userId) throws IOException {

        ChatDAO dao = new ChatDAO();

        int otherId = dao.getOtherParticipant(chatId, userId);
        if (otherId <= 0 || !dao.isChatAllowed(userId, otherId)) {
            session.close(new CloseReason(
                    CloseReason.CloseCodes.VIOLATED_POLICY,
                    "Unauthorized chat access"));
            return;
        }

        SESSIONS.put(userId, session);
        USER_IDS.put(session, userId);

        System.out.printf("WS OPEN  chat=%d  user=%d%n", chatId, userId);
    }

    @OnMessage
    public void onMessage(String json,
                          Session session,
                          @PathParam("chatId") int chatId,
                          @PathParam("userId") int userId) {

        try {

            Message msg = gson.fromJson(json, Message.class);
            msg.setChatId(chatId);
            msg.setSenderUserId(userId);
            msg.setSentAt(LocalDateTime.now());

            ChatDAO dao = new ChatDAO();
            dao.saveMessage(msg);

            int receiverId = dao.getOtherParticipant(chatId, userId);
            String payload = gson.toJson(msg);

            Session receiver = SESSIONS.get(receiverId);
            if (receiver != null && receiver.isOpen()) {
                receiver.getAsyncRemote().sendText(payload);
            }
            if (session.isOpen()) {
                session.getAsyncRemote().sendText(payload);    
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @OnClose
    public void onClose(Session session) {
        Integer uid = USER_IDS.remove(session);
        if (uid != null) {
            SESSIONS.remove(uid);
            System.out.printf("WS CLOSE user=%d%n", uid);
        }
    }

    @OnError
    public void onError(Session session, Throwable err) {
        System.err.println("WS ERROR: " + err.getMessage());
    }
}
