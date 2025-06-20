/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Workout {
    private int workoutId; 
    private LocalDate day;
    private LocalTime startTime;
    private LocalTime endTime;
    private int duration; // minute
    private ArrayList<Exercise> exercises;

    public Workout() {
    }

    public Workout(int workoutId, LocalDate day, LocalTime startTime, LocalTime endTime, int duration, ArrayList<Exercise> exercises) {
        this.workoutId = workoutId;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
        this.exercises = exercises;
    }

    public int getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(int workoutId) {
        this.workoutId = workoutId;
    }

    public ArrayList<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(ArrayList<Exercise> exercises) {
        this.exercises = exercises;
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
    
}
