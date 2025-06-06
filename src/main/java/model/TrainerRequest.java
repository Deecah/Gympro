package model;

import java.time.LocalDateTime;

public class TrainerRequest {
    private int trainerRequestID;
    private int trainerID;
    private String requestContent;
    private LocalDateTime submittedAt;
    private String status;
    private Integer adminID; // Có thể null
    private LocalDateTime processedAt; // Có thể null

    public TrainerRequest() {
    }

    public TrainerRequest(int trainerRequestID, int trainerID, String requestContent, LocalDateTime submittedAt, String status, Integer adminID, LocalDateTime processedAt) {
        this.trainerRequestID = trainerRequestID;
        this.trainerID = trainerID;
        this.requestContent = requestContent;
        this.submittedAt = submittedAt;
        this.status = status;
        this.adminID = adminID;
        this.processedAt = processedAt;
    }

    public int getTrainerRequestID() {
        return trainerRequestID;
    }

    public void setTrainerRequestID(int trainerRequestID) {
        this.trainerRequestID = trainerRequestID;
    }

    public int getTrainerID() {
        return trainerID;
    }

    public void setTrainerID(int trainerID) {
        this.trainerID = trainerID;
    }

    public String getRequestContent() {
        return requestContent;
    }

    public void setRequestContent(String requestContent) {
        this.requestContent = requestContent;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getAdminID() {
        return adminID;
    }

    public void setAdminID(Integer adminID) {
        this.adminID = adminID;
    }

    public LocalDateTime getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(LocalDateTime processedAt) {
        this.processedAt = processedAt;
    }

 
    
}
