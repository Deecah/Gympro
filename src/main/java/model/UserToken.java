package model;

import java.time.LocalDateTime;

public class UserToken {

    private int id;
    private int userId;
    private String token;
    private String tokenType;
    private LocalDateTime createdAt;
    private LocalDateTime expiry;
    private boolean isUsed;

    public UserToken() {
    }

    public UserToken(int id, int userId, String token, String tokenType, LocalDateTime expiry, boolean isUsed, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.token = token;
        this.tokenType = tokenType;
        this.expiry = expiry;
        this.isUsed = isUsed;
        this.createdAt = createdAt;
    }

    public UserToken(int userId, String token, String tokenType, LocalDateTime expiry, boolean isUsed, LocalDateTime createdAt) {
        this.userId = userId;
        this.token = token;
        this.tokenType = tokenType;
        this.expiry = expiry;
        this.isUsed = isUsed;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public LocalDateTime getExpiry() {
        return expiry;
    }

    public void setExpiry(LocalDateTime expiry) {
        this.expiry = expiry;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
