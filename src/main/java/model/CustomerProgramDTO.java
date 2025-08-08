package model;

import java.sql.Date;
import java.time.LocalDateTime;

public class CustomerProgramDTO {
    private int id;
    private int programId;
    private int customerId;
    private LocalDateTime assignedAt;
    private String customerName;
    private String avatarUrl;
    private String programName;
    private Date endDate;
    private Date startDate;
    private int scheduleId;

    public CustomerProgramDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProgramId() {
        return programId;
    }

    public void setProgramId(int programId) {
        this.programId = programId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(LocalDateTime assignedAt) {
        this.assignedAt = assignedAt;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    @Override
    public String toString() {
        return "CustomerProgramDTO{" +
                "id=" + id +
                ", programId=" + programId +
                ", customerId=" + customerId +
                ", assignedAt=" + assignedAt +
                ", customerName='" + customerName + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", programName='" + programName + '\'' +
                ", endDate=" + endDate +
                ", startDate=" + startDate +
                ", scheduleId=" + scheduleId +
                '}';
    }
}
