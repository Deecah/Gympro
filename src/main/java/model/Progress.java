package model;

import java.time.LocalDateTime;

public class Progress {
    private int progressID;
    private int customerProgramID;
    private double progressPercent;
    private LocalDateTime recordedAt;

    public int getProgressID() { return progressID; }
    public void setProgressID(int progressID) { this.progressID = progressID; }
    public int getCustomerProgramID() { return customerProgramID; }
    public void setCustomerProgramID(int customerProgramID) { this.customerProgramID = customerProgramID; }
    public double getProgressPercent() { return progressPercent; }
    public void setProgressPercent(double progressPercent) { this.progressPercent = progressPercent; }
    public LocalDateTime getRecordedAt() { return recordedAt; }
    public void setRecordedAt(LocalDateTime recordedAt) { this.recordedAt = recordedAt; }
}