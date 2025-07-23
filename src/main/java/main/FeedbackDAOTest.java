package main;
import dao.FeedbackDAO;
import java.util.List;
import model.Feedback;

public class FeedbackDAOTest {
    public static void main(String[] args) {
        FeedbackDAO dao = new FeedbackDAO();

        // Choose a packageId you know exists in your DB for testing
        int testPackageId = 4;

        List<Feedback> feedbacks = dao.getFeedbacksByPackageId(testPackageId);

        if (feedbacks.isEmpty()) {
            System.out.println("No feedbacks found for package ID " + testPackageId);
        } else {
            System.out.println("Feedbacks for package ID " + testPackageId + ":");
            for (Feedback fb : feedbacks) {
                System.out.println("User: " + fb.getUserName());
                System.out.println("Avatar: " + fb.getUserAvatar());
                System.out.println("Stars: " + fb.getStar());
                System.out.println("Content: " + fb.getFeedbackContent());
                System.out.println("---------------------------");
            }
        }
    }
}
