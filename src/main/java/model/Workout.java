package model;

import java.sql.Timestamp;

public class Workout {
    private int workoutID;
    private int dayID;
    private String title;
    private String rounds;
    private String notes;
    private Timestamp createdAt;

    public Workout() {}

    public int getWorkoutID() {
        return workoutID;
    }

    public void setWorkoutID(int workoutID) {
        this.workoutID = workoutID;
    }

    public int getDayID() {
        return dayID;
    }

    public void setDayID(int dayID) {
        this.dayID = dayID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRounds() {
        return rounds;
    }

    public void setRounds(String rounds) {
        this.rounds = rounds;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
