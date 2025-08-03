package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class CustomerWorkoutSchedule {
    private int scheduleId;
    private int customerProgramId;
    private int programDayId;
    private int workoutId;
    private LocalDate scheduledDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String status;

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public int getCustomerProgramId() {
        return customerProgramId;
    }

    public void setCustomerProgramId(int customerProgramId) {
        this.customerProgramId = customerProgramId;
    }

    public int getProgramDayId() {
        return programDayId;
    }

    public void setProgramDayId(int programDayId) {
        this.programDayId = programDayId;
    }

    public int getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(int workoutId) {
        this.workoutId = workoutId;
    }

    public LocalDate getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(LocalDate scheduledDate) {
        this.scheduledDate = scheduledDate;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
