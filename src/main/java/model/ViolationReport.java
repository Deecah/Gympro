/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDateTime;

public class ViolationReport {

    private int violationID;
    private int fromUserID;
    private int reportedUserID;
    private String reason;
    private LocalDateTime createdAt;

    public ViolationReport() {
    }

    public ViolationReport(int violationID, int reportedUserID, int fromUserID, String reason, LocalDateTime createdAt) {
        this.violationID = violationID;
        this.reportedUserID = reportedUserID;
        this.fromUserID = fromUserID;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}
