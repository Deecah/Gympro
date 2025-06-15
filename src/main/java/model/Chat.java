package model;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Chat {

    private int chatID;
    private int user1ID;
    private int user2ID;
    private ArrayList<Message> messages;
    private LocalDateTime startedAt;
    private LocalDateTime lastMessageAt;

    public Chat() {
    }

    public Chat(int chatID, int user1ID, int user2ID, ArrayList<Message> messages, LocalDateTime startedAt, LocalDateTime lastMessageAt) {
        this.chatID = chatID;
        this.user1ID = user1ID;
        this.user2ID = user2ID;
        this.messages = messages;
        this.startedAt = startedAt;
        this.lastMessageAt = lastMessageAt;
    }

    public int getChatID() {
        return chatID;
    }

    public void setChatID(int chatID) {
        this.chatID = chatID;
    }

    public int getUser1ID() {
        return user1ID;
    }

    public void setUser1ID(int user1ID) {
        this.user1ID = user1ID;
    }

    public int getUser2ID() {
        return user2ID;
    }

    public void setUser2ID(int user2ID) {
        this.user2ID = user2ID;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime getLastMessageAt() {
        return lastMessageAt;
    }

    public void setLastMessageAt(LocalDateTime lastMessageAt) {
        this.lastMessageAt = lastMessageAt;
    }

}
