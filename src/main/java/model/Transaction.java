
package model;

import java.time.LocalDateTime;

public class Transaction {
    private int id;
    private int contractID;
    private double amount;
    private LocalDateTime createdTime; //('pending', 'success', 'fail')) DEFAULT 'pending'
    private String status;
    private String description;

    public Transaction() {
    }

    public Transaction(int id, int contractID, double amount, LocalDateTime createdTime, String status, String description) {
        this.id = id;
        this.contractID = contractID;
        this.amount = amount;
        this.createdTime = createdTime;
        this.status = status;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getContractID() {
        return contractID;
    }

    public void setContractID(int contractID) {
        this.contractID = contractID;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
}
