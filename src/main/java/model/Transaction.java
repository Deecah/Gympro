package model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Transaction {
    private int transactionId;
    private int customerId;
    private BigDecimal amount;
    private Timestamp createdTime;
    private String status;

    public Transaction() {
    }

    public Transaction(int transactionId, int customerId, BigDecimal amount, Timestamp createdTime, String status) {
        this.transactionId = transactionId;
        this.customerId = customerId;
        this.amount = amount;
        this.createdTime = createdTime;
        this.status = status;
    }
    
    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}