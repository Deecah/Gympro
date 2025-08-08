package model;

public class CustomerProgressDTO {
    private int programId;
    private String programName;
    private int totalWorkouts;
    private int completedWorkouts;
    private double progressPercent;
    private String customerName;

    public int getProgramId() { return programId; }
    public void setProgramId(int programId) { this.programId = programId; }
    public String getProgramName() { return programName; }
    public void setProgramName(String programName) { this.programName = programName; }
    public int getTotalWorkouts() { return totalWorkouts; }
    public void setTotalWorkouts(int totalWorkouts) { this.totalWorkouts = totalWorkouts; }
    public int getCompletedWorkouts() { return completedWorkouts; }
    public void setCompletedWorkouts(int completedWorkouts) { this.completedWorkouts = completedWorkouts; }
    public double getProgressPercent() { return progressPercent; }
    public void setProgressPercent(double progressPercent) { this.progressPercent = progressPercent; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
}