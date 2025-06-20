/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.ArrayList;

public class Schedule {

    private int scheduleID;
    private int trainerID;
    private int customerID;
    private ArrayList<Workout> workouts; //t2 t3 t4

    public Schedule() {
    }
    
     public Schedule(int scheduleID, int trainerID, int customerID, ArrayList<Workout> workouts) {
        this.scheduleID = scheduleID;
        this.trainerID = trainerID;
        this.customerID = customerID;
        this.workouts = workouts;
    }

    public int getScheduleID() {
        return scheduleID;
    }

    public void setScheduleID(int scheduleID) {
        this.scheduleID = scheduleID;
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

    public ArrayList<Workout> getWorkouts() {
        return workouts;
    }

    public void setWorkouts(ArrayList<Workout> workouts) {
        this.workouts = workouts;
    }

}
