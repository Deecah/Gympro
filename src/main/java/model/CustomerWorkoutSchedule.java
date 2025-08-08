package model;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

public class CustomerWorkoutSchedule {
    private int scheduleId;
    private int customerProgramId;
    private int programId;
    private Date startAt;
    private Date endAt;
    private String status;
    private String programName;
    private String customerName;
    private int workoutId;
    private List<ExerciseProgram> exercises;
    private Date date;
    private Time startTime;
    private Time endTime;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

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

    public int getProgramId() {
        return programId;
    }

    public void setProgramId(int programId) {
        this.programId = programId;
    }

    public Date getStartAt() {
        return startAt;
    }

    public void setStartAt(Date startAt) {
        this.startAt = startAt;
    }

    public Date getEndAt() {
        return endAt;
    }

    public void setEndAt(Date endAt) {
        this.endAt = endAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(int workoutId) {
        this.workoutId = workoutId;
    }

    public List<ExerciseProgram> getExercises() {
        return exercises;
    }

    public void setExercises(List<ExerciseProgram> exercises) {
        this.exercises = exercises;
    }

    @Override
    public String toString() {
        return "CustomerWorkoutSchedule{" +
                "scheduleId=" + scheduleId +
                ", customerProgramId=" + customerProgramId +
                ", programId=" + programId +
                ", startAt=" + startAt +
                ", endAt=" + endAt +
                ", status='" + status + '\'' +
                ", programName='" + programName + '\'' +
                ", customerName='" + customerName + '\'' +
                ", workoutId=" + workoutId +
                ", exercises=" + exercises +
                ", date=" + date +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}