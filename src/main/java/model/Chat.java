package model;

public class Chat {
    private int chatId;
    private int userId1;
    private int userId2;
    private Integer messageId; 

    public Chat() {}

    public Chat(int chatId, int userId1, int userId2, Integer messageId) {
        this.chatId = chatId;
        this.userId1 = userId1;
        this.userId2 = userId2;
        this.messageId = messageId;
    }

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    public int getUserId1() {
        return userId1;
    }

    public void setUserId1(int userId1) {
        this.userId1 = userId1;
    }

    public int getUserId2() {
        return userId2;
    }

    public void setUserId2(int userId2) {
        this.userId2 = userId2;
    }

    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }
}
