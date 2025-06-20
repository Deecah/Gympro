package model;

import java.time.LocalDateTime;

public class Message {

    private int messageID;
    private int senderID;
    private String content;
    private LocalDateTime sendAt;
    private boolean isRead;

    public Message() {
    }

    public Message(int messageID, int senderID, String content, LocalDateTime sendAt, boolean isRead) {
        this.messageID = messageID;
        this.senderID = senderID;
        this.content = content;
        this.sendAt = sendAt;
        this.isRead = isRead;
    }

    public int getMessageID() {
        return messageID;
    }

    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }

    public int getSenderID() {
        return senderID;
    }

    public void setSenderID(int senderID) {
        this.senderID = senderID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getSendAt() {
        return sendAt;
    }

    public void setSendAt(LocalDateTime sendAt) {
        this.sendAt = sendAt;
    }

    public boolean isIsRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

}