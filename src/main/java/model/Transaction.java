package model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Transaction {

    private int transactionId;
    private int contractId;
    private BigDecimal amount;
    private Timestamp createdTime;
    private String status;
    private String description;
    private int customerId;

    public Transaction() {
    }

    public Transaction(int transactionId, int contractId, BigDecimal amount, Timestamp createdTime, String status, String description, int customerId) {
        this.transactionId = transactionId;
        this.contractId = contractId;
        this.amount = amount;
        this.createdTime = createdTime;
        this.status = status;
        this.description = description;
        this.customerId = customerId;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getContractId() {
        return contractId;
    }

    public void setContractId(int contractId) {
        this.contractId = contractId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
}
