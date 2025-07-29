package main;

import dao.ReportDAO;
import dao.UserDAO;
import model.ViolationReport;
import model.User;
import java.util.ArrayList;
import java.util.HashMap;

public class TestReportDAO {
    public static void main(String[] args) {
        System.out.println("=== BẮT ĐẦU TEST REPORT DAO ===");
        
        ReportDAO reportDAO = new ReportDAO();
        UserDAO userDAO = new UserDAO();
        
        // Test 1: Kiểm tra số lượng báo cáo trong database
        System.out.println("\n1. Kiểm tra số lượng báo cáo:");
        int reportCount = reportDAO.getReportCount();
        System.out.println("Số lượng báo cáo: " + reportCount);
        
        // Test 2: Lấy tất cả báo cáo
        System.out.println("\n2. Lấy tất cả báo cáo:");
        ArrayList<ViolationReport> reportList = reportDAO.getAllReports();
        System.out.println("Danh sách báo cáo: " + (reportList != null ? reportList.size() : "null"));
        
        if (reportList != null && !reportList.isEmpty()) {
            for (ViolationReport report : reportList) {
                System.out.println("Báo cáo ID: " + report.getViolationID());
                System.out.println("  - Từ user ID: " + report.getFromUserID());
                System.out.println("  - Báo cáo user ID: " + report.getReportedUserID());
                System.out.println("  - Lý do: " + report.getReason());
                System.out.println("  - Thời gian: " + report.getCreatedAt());
                
                // Test lấy thông tin user
                User fromUser = userDAO.getUserById(report.getFromUserID());
                User reportedUser = userDAO.getUserById(report.getReportedUserID());
                
                System.out.println("  - Từ user: " + (fromUser != null ? fromUser.getUserName() : "Không tìm thấy"));
                System.out.println("  - Bị báo cáo: " + (reportedUser != null ? reportedUser.getUserName() : "Không tìm thấy"));
                System.out.println("---");
            }
        } else {
            System.out.println("Không có báo cáo nào trong database");
        }
        
        // Test 3: Tạo một báo cáo test
        System.out.println("\n3. Tạo báo cáo test:");
        boolean created = reportDAO.createReport(1, 2, "Test violation report");
        System.out.println("Tạo báo cáo test: " + (created ? "thành công" : "thất bại"));
        
        // Test 4: Kiểm tra lại sau khi tạo
        System.out.println("\n4. Kiểm tra lại sau khi tạo:");
        reportList = reportDAO.getAllReports();
        System.out.println("Số lượng báo cáo sau khi tạo: " + (reportList != null ? reportList.size() : "null"));
        
        System.out.println("\n=== KẾT THÚC TEST REPORT DAO ===");
    }
} 