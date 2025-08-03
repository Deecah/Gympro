package model;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class CustomerProgramDTO {
    private int id;
    private int programId;
    private int customerId;
    private LocalDateTime assignedAt;
    private String customerName;
    private String avatarUrl;
    private String programName;

    public CustomerProgramDTO() {
    }

    public CustomerProgramDTO(int id, int programId, int customerId, LocalDateTime assignedAt, String customerName, String avatarUrl, String programName) {
        this.id = id;
        this.programId = programId;
        this.customerId = customerId;
        this.assignedAt = assignedAt;
        this.customerName = customerName;
        this.avatarUrl = avatarUrl;
        this.programName = programName;
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
                '}';
    }
}
