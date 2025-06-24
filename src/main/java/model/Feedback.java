
package model;

public class Feedback {
    private int feedbackID;
    private int userID;
    private String feedbackType; // 'trainer' hoặc 'package'
    private String feedbackContent;
    private int point; // 0–5

    public Feedback() {
    }

    public Feedback(int feedbackID, int userID, String feedbackType, String feedbackContent, int point) {
        this.feedbackID = feedbackID;
        this.userID = userID;
        this.feedbackType = feedbackType;
        this.feedbackContent = feedbackContent;
        this.point = point;
    }

    public int getFeedbackID() {
        return feedbackID;
    }

    public void setFeedbackID(int feedbackID) {
        this.feedbackID = feedbackID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getFeedbackType() {
        return feedbackType;
    }

    public void setFeedbackType(String feedbackType) {
        this.feedbackType = feedbackType;
    }

    public String getFeedbackContent() {
        return feedbackContent;
    }

    public void setFeedbackContent(String feedbackContent) {
        this.feedbackContent = feedbackContent;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    
    
}