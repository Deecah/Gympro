package model;


import java.time.LocalTime;

public class Schedule {

    private int scheduleID;
    private int trainerID;
    private int userID;
    private String weekday; //t2 t3 t4
    private int duration; // minute
    private LocalTime startTime;
    private LocalTime endTime;

    public Schedule() {
    }

    public Schedule(int scheduleID, int trainerID, int userID, String weekday,int duration, LocalTime startTime, LocalTime endTime) {
        this.scheduleID = scheduleID;
        this.trainerID = trainerID;
        this.userID = userID;
        this.weekday = weekday;
        this.duration = duration;
        this.startTime = startTime;
        this.endTime = endTime;
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

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getWeekday() {
        return weekday;
    }

    public void setWeekday(String weekday) {
        this.weekday = weekday;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
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


}