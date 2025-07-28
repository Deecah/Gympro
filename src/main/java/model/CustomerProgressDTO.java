package model;

public class CustomerProgressDTO {
    private User customer;
    private double completionRate;
    private int totalWorkouts;
    private int completedWorkouts;
    private String nextDue;
    private String groupName;
    private int programId;
    private String programName;
    private double progressPercent;

    public CustomerProgressDTO() {
    }

    public CustomerProgressDTO(User customer, double completionRate, int totalWorkouts, int completedWorkouts, String nextDue) {
        this.customer = customer;
        this.completionRate = completionRate;
        this.totalWorkouts = totalWorkouts;
        this.completedWorkouts = completedWorkouts;
        this.nextDue = nextDue;
    }

    // Getters and Setters
    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public double getCompletionRate() {
        return completionRate;
    }

    public void setCompletionRate(double completionRate) {
        this.completionRate = completionRate;
    }

    public int getTotalWorkouts() {
        return totalWorkouts;
    }

    public void setTotalWorkouts(int totalWorkouts) {
        this.totalWorkouts = totalWorkouts;
    }

    public int getCompletedWorkouts() {
        return completedWorkouts;
    }

    public void setCompletedWorkouts(int completedWorkouts) {
        this.completedWorkouts = completedWorkouts;
    }

    public String getNextDue() {
        return nextDue;
    }

    public void setNextDue(String nextDue) {
        this.nextDue = nextDue;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    
    public int getProgramId() {
        return programId;
    }
    
    public void setProgramId(int programId) {
        this.programId = programId;
    }
    
    public String getProgramName() {
        return programName;
    }
    
    public void setProgramName(String programName) {
        this.programName = programName;
    }
    
    public double getProgressPercent() {
        return progressPercent;
    }
    
    public void setProgressPercent(double progressPercent) {
        this.progressPercent = progressPercent;
    }

    // Helper methods for JSP
    public String getCustomerName() {
        return customer != null ? customer.getUserName() : "";
    }

    public String getCustomerEmail() {
        return customer != null ? customer.getEmail() : "";
    }

    public String getCustomerAvatar() {
        return customer != null ? customer.getAvatarUrl() : "";
    }

    public int getCustomerId() {
        return customer != null ? customer.getUserId() : 0;
    }

    public String getCompletionRateFormatted() {
        return String.format("%.1f", completionRate);
    }

    public String getProgressColor() {
        if (completionRate >= 80) return "success";
        if (completionRate >= 60) return "warning";
        return "danger";
    }
} 