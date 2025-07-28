package model;

import java.time.LocalDateTime;
import java.util.List;

public class WorkoutPostDTO {
    private User user;
    private Workout workout;
    private List<Exercise> exercises;
    
    // Additional fields for progress tracking
    private int workoutId;
    private String workoutName;
    private String description;
    private String dayName;
    private int weekNumber;
    private boolean completed;
    private LocalDateTime scheduledTime;
    private LocalDateTime completedAt;
    private String notes;

    // Original getters and setters
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Workout getWorkout() { return workout; }
    public void setWorkout(Workout workout) { this.workout = workout; }

    public List<Exercise> getExercises() { return exercises; }
    public void setExercises(List<Exercise> exercises) { this.exercises = exercises; }
    
    // New getters and setters for progress tracking
    public int getWorkoutId() { return workoutId; }
    public void setWorkoutId(int workoutId) { this.workoutId = workoutId; }
    
    public String getWorkoutName() { return workoutName; }
    public void setWorkoutName(String workoutName) { this.workoutName = workoutName; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getDayName() { return dayName; }
    public void setDayName(String dayName) { this.dayName = dayName; }
    
    public int getWeekNumber() { return weekNumber; }
    public void setWeekNumber(int weekNumber) { this.weekNumber = weekNumber; }
    
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
    
    public LocalDateTime getScheduledTime() { return scheduledTime; }
    public void setScheduledTime(LocalDateTime scheduledTime) { this.scheduledTime = scheduledTime; }
    
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    // Helper methods
    public String getScheduledTimeFormatted() {
        if (scheduledTime != null) {
            return scheduledTime.format(java.time.format.DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm"));
        }
        return "";
    }
    
    public String getCompletedAtFormatted() {
        if (completedAt != null) {
            return completedAt.format(java.time.format.DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm"));
        }
        return "";
    }
    
    public String getStatusColor() {
        return completed ? "success" : "warning";
    }
    
    public String getStatusText() {
        return completed ? "Completed" : "Pending";
    }
} 