package model;

import java.sql.Timestamp;
import java.time.LocalTime;

public class Workout {

    private int workoutID;
    private int dayID;
    private String title;
    private String notes;
    private Timestamp createdAt;
    private LocalTime startTime;
    private LocalTime endTime;
    private int startHour;
    private String startStr;
    private String endStr;
    private String programName;

    // Getters and Setters
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

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public int getStartHour() {
        return startHour;
    }

    public void setStartStr(String startStr) {
        this.startStr = startStr;
    }

    public String getStartStr() {
        return startStr;
    }

    public void setEndStr(String endStr) {
        this.endStr = endStr;
    }

    public String getEndStr() {
        return endStr;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

}
