package model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Slot {
    private int slotId; 
    private LocalDate day;
    private LocalTime startTime;
    private LocalTime endTime;
    private int duration; // in minutes
    private ArrayList<Exercise> exercises;



    public Slot() {
    }

    public Slot(int slotId, LocalDate day, LocalTime startTime, LocalTime endTime, int duration,
                ArrayList<Exercise> exercises) {
        this.slotId = slotId;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
        this.exercises = exercises;
    }

    public int getSlotId() {
        return slotId;
    }

    public void setSlotId(int slotId) {
        this.slotId = slotId;
    }

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public ArrayList<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(ArrayList<Exercise> exercises) {
        this.exercises = exercises;
    }

    
 
}
