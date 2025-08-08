package model;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class CustomerProgram {
    private int id;
    private int programId;
    private int customerId;
    private LocalDateTime assignedAt;
    private Date startDate;
    private Date endDate;

    public CustomerProgram() {
    }

    public CustomerProgram(int id, int programId, int customerId, LocalDateTime assignedAt, Date startDate, Date endDate) {
        this.id = id;
        this.programId = programId;
        this.customerId = customerId;
        this.assignedAt = assignedAt;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Getter & Setter
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
