package model;

import java.time.LocalDateTime;

public class Request {
    private int requestID;
    private int trainerID;
    private int customerID;
    private LocalDateTime desiredDate;
    private LocalDateTime requestedAt;
    private String status; // 4 trạng thái: pending, accepted, rejected, cancelled
    private LocalDateTime replyAt; // Có thể null

    public Request() {
    }

    public Request(int requestID, int trainerID, int customerID, LocalDateTime desiredDate, LocalDateTime requestedAt, String status, LocalDateTime replyAt) {
        this.requestID = requestID;
        this.trainerID = trainerID;
        this.customerID = customerID;
        this.desiredDate = desiredDate;
        this.requestedAt = requestedAt;
        this.status = status;
        this.replyAt = replyAt;
    }

    public int getRequestID() {
        return requestID;
    }

    public void setRequestID(int requestID) {
        this.requestID = requestID;
    }

    public int getTrainerID() {
        return trainerID;
    }

    public void setTrainerID(int trainerID) {
        this.trainerID = trainerID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public LocalDateTime getDesiredDate() {
        return desiredDate;
    }

    public void setDesiredDate(LocalDateTime desiredDate) {
        this.desiredDate = desiredDate;
    }

    public LocalDateTime getRequestedAt() {
        return requestedAt;
    }

    public void setRequestedAt(LocalDateTime requestedAt) {
        this.requestedAt = requestedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getReplyAt() {
        return replyAt;
    }

    public void setReplyAt(LocalDateTime replyAt) {
        this.replyAt = replyAt;
    }
    
}
