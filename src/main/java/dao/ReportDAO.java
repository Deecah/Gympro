package dao;

import connectDB.ConnectDatabase;
import java.sql.*;
import java.util.ArrayList;
import model.ViolationReport;

public class ReportDAO {

    // Lấy tất cả báo cáo
    public ArrayList<ViolationReport> getAllReports() {
        ArrayList<ViolationReport> reportList = new ArrayList<>();
        String sql = "SELECT violationID, fromUserID, reportedUserID, reason, createdAt FROM ViolationReport";

        try (Connection conn = ConnectDatabase.getInstance().openConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ViolationReport report = new ViolationReport();
                report.setViolationID(rs.getInt("violationID"));
                report.setFromUserID(rs.getInt("fromUserID"));
                report.setReportedUserID(rs.getInt("reportedUserID"));
                report.setReason(rs.getString("reason"));

                Timestamp ts = rs.getTimestamp("createdAt");
                if (ts != null) {
                    report.setCreatedAt(new java.util.Date(ts.getTime()));
                }
                reportList.add(report);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reportList;
    }

    // Tạo một báo cáo mới
    public boolean createReport(int fromUserId, int reportedUserId, String reason) {
        String sql = "INSERT INTO ViolationReport (fromUserID, reportedUserID, reason, createdAt) VALUES (?, ?, ?, GETDATE())";

        try (Connection conn = ConnectDatabase.getInstance().openConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, fromUserId);
            ps.setInt(2, reportedUserId);
            ps.setString(3, reason);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Xóa báo cáo theo ID
    public boolean deleteReportById(int violationId) {
        String sql = "DELETE FROM ViolationReport WHERE violationID = ?";

        try (Connection conn = ConnectDatabase.getInstance().openConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, violationId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
