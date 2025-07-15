package model;

import java.util.Date;

public class ViolationReport {

    private int violationID;
    private int fromUserID;
    private int reportedUserID;
    private String reason;
    private Date createdAt;
    private Date lastEdit;

    public ViolationReport() {
    }

    public ViolationReport(int violationID, int fromUserID, int reportedUserID, String reason, Date createdAt, Date lastEdit) {
        this.violationID = violationID;
        this.fromUserID = fromUserID;
        this.reportedUserID = reportedUserID;
        this.reason = reason;
        this.createdAt = createdAt;
        this.lastEdit = lastEdit;
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

    public int getFromUserID() {
        return fromUserID;
    }

    public void setFromUserID(int fromUserID) {
        this.fromUserID = fromUserID;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    public Date getLastEdit() {
        return lastEdit;
    }

    public void setLastEdit(Date lastEdit) {
        this.lastEdit = lastEdit;
    }
}