
package model;

import java.time.LocalDateTime;

public class ViolationReport {
    private int violationID;
    private int reportedUserID;
    private int userID;
    private String reason;
    private LocalDateTime createdAt;

    public ViolationReport() {
    }

    public ViolationReport(int violationID, int reportedUserID, int userID, String reason, LocalDateTime createdAt) {
        this.violationID = violationID;
        this.reportedUserID = reportedUserID;
        this.userID = userID;
        this.reason = reason;
        this.createdAt = createdAt;
    }

    public int getViolationID() {
        return violationID;
    }

    public void setViolationID(int violationID) {
        this.violationID = violationID;
    }

    public int getReportedUserID() {
        return reportedUserID;
    }

    public void setReportedUserID(int reportedUserID) {
        this.reportedUserID = reportedUserID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }


}