//package main;
//
//import dao.ProgramDAO;
//import dao.ProgressDAO;
//import model.Program;
//
//import java.util.List;
//import model.Progress;
//
//public class TestProgramDAO {
//    public static void main(String[] args) {
//            int testUserId = 1; // ✅ Giả lập userId
//
//        List<Progress> progressList = ProgressDAO.getProgressByUserID(testUserId);
//
//        System.out.println("UserID in test: " + testUserId);
//        System.out.println("Progress list size: " + (progressList != null ? progressList.size() : 0));
//
//        if (progressList != null && !progressList.isEmpty()) {
//            for (Progress p : progressList) {
//                System.out.println("ProgressID: " + p.getProgressID());
//                System.out.println("UserID: " + p.getUserID());
//                System.out.println("RecordedAt: " + p.getRecordedAt());
//                System.out.println("Weight: " + p.getWeight());
//                System.out.println("Body Fat %: " + p.getBodyFatPercent());
//                System.out.println("Muscle Mass: " + p.getMuscleMass());
//                System.out.println("Notes: " + p.getNotes());
//                System.out.println("----------------------------");
//            }
//        } else {
//            System.out.println("No progress records found for user.");
//        }
//    }
//}
