package model;

import java.time.LocalDateTime;

public class Notification {
    private int notificationID;
    private int userID;
    private String title;
    private String content;
    private LocalDateTime createdTime;

    public Notification() {
    }

    public Notification(int notificationID, int userID, String title, String content, LocalDateTime createdTime) {
        this.notificationID = notificationID;
        this.userID = userID;
        this.title = title;
        this.content = content;
        this.createdTime = createdTime;
    }

    public int getNotificationID() {
        return notificationID;
    }

    public void setNotificationID(int notificationID) {
        this.notificationID = notificationID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }
 
    
}