package dao;

import connectDB.ConnectDatabase;
import java.sql.*;
import java.util.ArrayList;
import model.ViolationReport;

public class ReportDAO {

    // Lấy tất cả báo cáo
    public ArrayList<ViolationReport> getAllReports() {
        ArrayList<ViolationReport> reportList = new ArrayList<>();
        String sql = "SELECT ViolationID, UserID, ReportedUserID, Reason, CreatedAt FROM ViolationReport ORDER BY CreatedAt DESC";

        try (Connection conn = ConnectDatabase.getInstance().openConnection()) {
            if (conn == null) {
                System.out.println("Không thể kết nối database");
                return reportList;
            }
            
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                try (ResultSet rs = ps.executeQuery()) {
                    System.out.println("Đang thực thi query: " + sql);
                    
                    while (rs.next()) {
                        ViolationReport report = new ViolationReport();
                        report.setViolationID(rs.getInt("ViolationID"));
                        report.setFromUserID(rs.getInt("UserID")); // UserID = người gửi report
                        report.setReportedUserID(rs.getInt("ReportedUserID")); // ReportedUserID = người bị report
                        report.setReason(rs.getString("Reason"));

                        Timestamp ts = rs.getTimestamp("CreatedAt");
                        if (ts != null) {
                            report.setCreatedAt(new java.util.Date(ts.getTime()));
                        }
                        
                        reportList.add(report);
                        System.out.println("Đã thêm báo cáo: ID=" + report.getViolationID() + 
                                         ", From=" + report.getFromUserID() + 
                                         ", To=" + report.getReportedUserID() + 
                                         ", Reason=" + report.getReason());
                    }
                }
            }
            
            System.out.println("Tổng số báo cáo lấy được: " + reportList.size());
            
        } catch (SQLException e) {
            System.out.println("Lỗi SQL trong getAllReports: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Lỗi trong getAllReports: " + e.getMessage());
            e.printStackTrace();
        }
        
        return reportList;
    }

    // Tạo một báo cáo mới
    public boolean createReport(int fromUserId, int reportedUserId, String reason) {
        String sql = "INSERT INTO ViolationReport (UserID, ReportedUserID, Reason, CreatedAt) VALUES (?, ?, ?, GETDATE())";

        try (Connection conn = ConnectDatabase.getInstance().openConnection()) {
            if (conn == null) {
                System.out.println("Không thể kết nối database");
                return false;
            }
            
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, fromUserId); // UserID = người gửi report
                ps.setInt(2, reportedUserId); // ReportedUserID = người bị report
                ps.setString(3, reason);

                int rowsAffected = ps.executeUpdate();
                System.out.println("Đã tạo báo cáo mới: " + (rowsAffected > 0 ? "thành công" : "thất bại"));
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            System.out.println("Lỗi SQL trong createReport: " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.out.println("Lỗi trong createReport: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Xóa báo cáo theo ID
    public boolean deleteReportById(int violationId) {
        String sql = "DELETE FROM ViolationReport WHERE ViolationID = ?";

        try (Connection conn = ConnectDatabase.getInstance().openConnection()) {
            if (conn == null) {
                System.out.println("Không thể kết nối database");
                return false;
            }
            
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, violationId);

                int rowsAffected = ps.executeUpdate();
                System.out.println("Đã xóa báo cáo ID " + violationId + ": " + (rowsAffected > 0 ? "thành công" : "thất bại"));
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            System.out.println("Lỗi SQL trong deleteReportById: " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.out.println("Lỗi trong deleteReportById: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // Kiểm tra xem có báo cáo nào không
    public int getReportCount() {
        String sql = "SELECT COUNT(*) FROM ViolationReport";
        
        try (Connection conn = ConnectDatabase.getInstance().openConnection()) {
            if (conn == null) {
                System.out.println("Không thể kết nối database");
                return 0;
            }
            
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        int count = rs.getInt(1);
                        System.out.println("Số lượng báo cáo trong database: " + count);
                        return count;
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Lỗi SQL trong getReportCount: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Lỗi trong getReportCount: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
}
