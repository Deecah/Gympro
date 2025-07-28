package model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CustomerProgram {
    private int id;
    private int programId;
    private int customerId;
    private LocalDateTime assignedAt;
    private LocalDate startDate;

    public CustomerProgram() {
    }

    public CustomerProgram(int id, int programId, int customerId, LocalDateTime assignedAt, LocalDate startDate) {
        this.id = id;
        this.programId = programId;
        this.customerId = customerId;
        this.assignedAt = assignedAt;
        this.startDate = startDate;
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

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
}
