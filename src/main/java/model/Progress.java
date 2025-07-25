package model;

import java.time.LocalDateTime;

public class Progress {

    private int progressID;
    private int userId;
    private int workoutID;
    private LocalDateTime recordedAt;
    private double weight;
    private double bodyFatPercent;
    private double muscleMass;
    private String notes;

    public Progress() {
    }

    public Progress(int progressID, int customerID, int trainerID, int workoutID, LocalDateTime recordedAt, double weight, double bodyFatPercent, double muscleMass, String notes) {
        this.progressID = progressID;
        this.userId = userId;
        this.workoutID = workoutID;
        this.recordedAt = recordedAt;
        this.weight = weight;
        this.bodyFatPercent = bodyFatPercent;
        this.muscleMass = muscleMass;
        this.notes = notes;
    }

    public int getProgressID() {
        return progressID;
    }

    public void setProgressID(int progressID) {
        this.progressID = progressID;
    }

    public LocalDateTime getRecordedAt() {
        return recordedAt;
    }

    public void setRecordedAt(LocalDateTime recordedAt) {
        this.recordedAt = recordedAt;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getBodyFatPercent() {
        return bodyFatPercent;
    }

    public void setBodyFatPercent(double bodyFatPercent) {
        this.bodyFatPercent = bodyFatPercent;
    }

    public double getMuscleMass() {
        return muscleMass;
    }

    public void setMuscleMass(double muscleMass) {
        this.muscleMass = muscleMass;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getUserID() {
        return userId;
    }

    public void setUserID(int userID) {
        this.userId = userID;
    }

    public int getWorkoutID() {
        return workoutID;
    }

    public void setWorkoutID(int workoutID) {
        this.workoutID = workoutID;
    }
}
