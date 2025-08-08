package model;

import java.sql.Timestamp;

public class Program {
    private int programId;
    private String name;
    private String description;
    private int trainerId;
    private Integer packageId;
    private Timestamp createdAt;

    public Program() {
    }
    
    public Program(int programId, String name, String description) {
        this.programId = programId;
        this.name = name;
        this.description = description;
    }

    public Program(int programId, String name, String description, int trainerId, Integer packageId, Timestamp createdAt) {
        this.programId = programId;
        this.name = name;
        this.description = description;
        this.trainerId = trainerId;
        this.packageId = packageId;
        this.createdAt = createdAt;
    }

    public int getProgramId() {
        return programId;
    }

    public void setProgramId(int programId) {
        this.programId = programId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(int trainerId) {
        this.trainerId = trainerId;
    }

    public Integer getPackageId() {
        return packageId;
    }

    public void setPackageId(Integer packageId) {
        this.packageId = packageId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    
 
}