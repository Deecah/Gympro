package model;

import java.sql.Date;
import java.time.LocalDateTime;

public class Contract {
    private int id;
    private int trainerID;
    private int customerID;
    private int packageID;
    private Date startDate;
    private Date endDate;
    private String status; 

    public Contract(int id, int trainerID, int customerID, int packageID, Date startDate, Date endDate, String status) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
      
}

