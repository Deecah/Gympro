package model;

import java.util.ArrayList;

public class Schedule {

    private int scheduleID;
    private int trainerID;
    private int customerID;
    private ArrayList<Slot> slots; // t2 t3 t4

    public Schedule() {
    }

    public Schedule(int scheduleID, int trainerID, int customerID, ArrayList<Slot> slots) {
        this.scheduleID = scheduleID;
        this.trainerID = trainerID;
        this.customerID = customerID;
        this.slots = slots;
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

    public ArrayList<Slot> getSlots() {
        return slots;
    }

    public void setSlots(ArrayList<Slot> slots) {
        this.slots = slots;
    }
    

}
