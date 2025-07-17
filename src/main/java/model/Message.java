package model;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class Message {

    private int messageId;
    private int chatId;
    private int senderUserId;
    private String messageContent;
    private String imageUrl;
    private String fileUrl;
    private LocalDateTime sentAt;


    public Message() {
    }

    public Message(int messageId, int chatId, int senderUserId, String messageContent, String imageUrl, String fileUrl, LocalDateTime sentAt) {
        this.messageId = messageId;
        this.chatId = chatId;
        this.senderUserId = senderUserId;
        this.messageContent = messageContent;
        this.imageUrl = imageUrl;
        this.fileUrl = fileUrl;
        this.sentAt = sentAt;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    public int getSenderUserId() {
        return senderUserId;
    }

    public void setSenderUserId(int senderUserId) {
        this.senderUserId = senderUserId;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public Date getSentAtDate() {
        return Date.from(sentAt.atZone(ZoneId.systemDefault()).toInstant());
    }

}
