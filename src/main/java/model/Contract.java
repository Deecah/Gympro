
package model;

import java.time.LocalDateTime;

public class Contract {
    private int id;
    private int trainerID;
    private int customerID;
    private int packageID;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String status; // 4 trang thai bao gom : pending, active, completed, cancelled

    public Contract(int id, int trainerID, int customerID, int packageID, LocalDateTime startDate, LocalDateTime endDate, String status) {
        this.id = id;
        this.trainerID = trainerID;
        this.customerID = customerID;
        this.packageID = packageID;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    public Contract() {
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTrainerID() {
        return trainerID;
    }

    public void setTrainerID(int trainerID) {
        this.trainerID = trainerID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public int getPackageID() {
        return packageID;
    }

    public void setPackageID(int packageID) {
        this.packageID = packageID;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
       
}