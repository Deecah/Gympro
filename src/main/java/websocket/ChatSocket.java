// src/main/java/websocket/ChatSocket.java
    package websocket;

    import com.google.gson.Gson;
    import com.google.gson.GsonBuilder;
    import com.google.gson.JsonObject;
    import com.google.gson.JsonSerializer;
    import com.google.gson.JsonPrimitive;
    import dao.ChatDAO;
    import model.Message;

    import jakarta.websocket.*;
    import jakarta.websocket.server.PathParam;
    import jakarta.websocket.server.ServerEndpoint;

    import java.io.IOException;
    import java.time.LocalDateTime;
    import java.util.Collections;
    import java.util.HashMap;
    import java.util.Map;

    @ServerEndpoint(value = "/chatendpoint/{roomId}")
    public class ChatSocket {

        private static final Map<String, Map<Session, String>> rooms = Collections.synchronizedMap(new HashMap<>());
        private static final Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (src, typeOfSrc, context) -> new JsonPrimitive(src.toString()))
                .create();

        @OnOpen
        public void onOpen(Session session, @PathParam("roomId") String roomId) {
            Map<Session, String> roomClients = rooms.computeIfAbsent(roomId, k -> Collections.synchronizedMap(new HashMap<>()));
            roomClients.put(session, roomId);
            rooms.put(roomId, roomClients);
        }

        @OnMessage
        public void onMessage(String json, Session session, @PathParam("roomId") String roomId) {
            try {
                JsonObject obj = gson.fromJson(json, JsonObject.class);

                // Handle initial load message
                if (obj.has("load")) {
                    // Optionally send chat history here
                    return;
                }

                if (obj.has("message")) {
                    JsonObject msgObj = obj.getAsJsonObject("message");
                    Message msg = new Message();
                    msg.setChatId(roomId.hashCode());
                    msg.setSenderUserId(msgObj.get("userId").getAsInt());
                    msg.setMessageContent(msgObj.get("content").getAsString());
                    msg.setImageUrl(msgObj.has("imageUrl") && !msgObj.get("imageUrl").isJsonNull() ? msgObj.get("imageUrl").getAsString() : null);
                    msg.setSentAt(LocalDateTime.now());

                    ChatDAO dao = new ChatDAO();
                    dao.saveMessage(msg);

                    String payload = gson.toJson(msg);

                    Map<Session, String> roomClients = rooms.get(roomId);
                    for (Session client : roomClients.keySet()) {
                        if (client.isOpen()) {
                            client.getBasicRemote().sendText(payload);
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @OnClose
        public void onClose(Session session, @PathParam("roomId") String roomId) {
            Map<Session, String> roomClients = rooms.get(roomId);
            if (roomClients != null) {
                roomClients.remove(session);
                if (roomClients.isEmpty()) {
                    rooms.remove(roomId);
                }
            }
        }

        @OnError
        public void onError(Session session, Throwable err) {
            System.err.println("WS ERROR: " + err.getMessage());
        }
    }