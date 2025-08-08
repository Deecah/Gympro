
package dao;

import connectDB.ConnectDatabase;
import model.CustomerProgressDTO;
import model.Progress;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProgressDAO {

    /**
     * Tính và lưu phần trăm hoàn thành của CustomerProgram
     * @param customerProgramId ID của CustomerProgram
     * @return true nếu lưu thành công, false nếu thất bại
     */
    public boolean updateProgressPercent(int customerProgramId) {
        String sql = "MERGE INTO Progress AS target " +
                "USING (SELECT ? AS CustomerProgramID, ? AS ProgressPercent, GETDATE() AS RecordedAt) AS source " +
                "ON target.CustomerProgramID = source.CustomerProgramID " +
                "WHEN MATCHED THEN " +
                "    UPDATE SET ProgressPercent = source.ProgressPercent, RecordedAt = source.RecordedAt " +
                "WHEN NOT MATCHED THEN " +
                "    INSERT (CustomerProgramID, ProgressPercent, RecordedAt) " +
                "    VALUES (source.CustomerProgramID, source.ProgressPercent, source.RecordedAt);";

        double progressPercent = calculateProgressPercent(customerProgramId);
        try (Connection conn = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerProgramId);
            ps.setDouble(2, progressPercent);
            return ps.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Tính phần trăm hoàn thành của CustomerProgram
     * @param customerProgramId ID của CustomerProgram
     * @return Phần trăm hoàn thành (0.0 - 100.0)
     */
    private double calculateProgressPercent(int customerProgramId) {
        int totalWorkouts = getTotalWorkoutsForCustomerProgram(customerProgramId);
        int completedWorkouts = getCompletedWorkoutsForCustomerProgram(customerProgramId);
        if (totalWorkouts == 0) {
            return 0.0;
        }
        return Math.round(((double) completedWorkouts / totalWorkouts * 100) * 100.0) / 100.0;
    }

    /**
     * Lấy tổng số bài tập cho CustomerProgram
     * @param customerProgramId ID của CustomerProgram
     * @return Số lượng workout
     */
    public int getTotalWorkoutsForCustomerProgram(int customerProgramId) {
        String sql = "SELECT COUNT(w.WorkoutID) as total " +
                "FROM Workout w " +
                "JOIN CustomerWorkoutSchedule cws ON w.ScheduleID = cws.ScheduleID " +
                "WHERE cws.CustomerProgramID = ?";

        try (Connection conn = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerProgramId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Lấy số bài tập đã hoàn thành cho CustomerProgram
     * @param customerProgramId ID của CustomerProgram
     * @return Số lượng workout đã hoàn thành
     */
    public int getCompletedWorkoutsForCustomerProgram(int customerProgramId) {
        String sql = "SELECT COUNT(w.WorkoutID) as completed " +
                "FROM Workout w " +
                "JOIN CustomerWorkoutSchedule cws ON w.ScheduleID = cws.ScheduleID " +
                "WHERE cws.CustomerProgramID = ? AND cws.Status = 'completed'";

        try (Connection conn = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerProgramId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("completed");
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Lấy thống kê tiến độ chương trình cho khách hàng
     * @param customerId ID của khách hàng
     * @return Danh sách CustomerProgressDTO
     */
    public List<CustomerProgressDTO> getProgramProgressStats(int customerId) {
        List<CustomerProgressDTO> progressStats = new ArrayList<>();
        String sql = "SELECT cp.Id AS CustomerProgramID, p.ProgramID, p.Name AS ProgramName, " +
                "COUNT(w.WorkoutID) AS TotalWorkouts " +
                "FROM CustomerProgram cp " +
                "JOIN Program p ON cp.ProgramID = p.ProgramID " +
                "LEFT JOIN CustomerWorkoutSchedule cws ON cp.Id = cws.CustomerProgramID " +
                "LEFT JOIN Workout w ON cws.ScheduleID = w.ScheduleID " +
                "WHERE cp.CustomerID = ? " +
                "GROUP BY cp.Id, p.ProgramID, p.Name " +
                "ORDER BY MAX(w.Date) DESC";

        try (Connection conn = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CustomerProgressDTO dto = new CustomerProgressDTO();
                    int customerProgramId = rs.getInt("CustomerProgramID");
                    dto.setProgramId(rs.getInt("ProgramID"));
                    dto.setProgramName(rs.getString("ProgramName"));
                    dto.setTotalWorkouts(rs.getInt("TotalWorkouts"));

                    // Tính số bài tập đã hoàn thành
                    int completedWorkouts = getCompletedWorkoutsForCustomerProgram(customerProgramId);
                    dto.setCompletedWorkouts(completedWorkouts);

                    // Lấy phần trăm tiến độ từ bảng Progress
                    double progressPercent = getLatestProgressPercent(customerProgramId);
                    dto.setProgressPercent(progressPercent);

                    progressStats.add(dto);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return progressStats;
    }

    /**
     * Lấy phần trăm tiến độ mới nhất của CustomerProgram
     * @param customerProgramId ID của CustomerProgram
     * @return Phần trăm tiến độ hoặc 0.0 nếu không có
     */
    private double getLatestProgressPercent(int customerProgramId) {
        String sql = "SELECT TOP 1 ProgressPercent FROM Progress " +
                "WHERE CustomerProgramID = ? ORDER BY RecordedAt DESC";

        try (Connection conn = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerProgramId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("ProgressPercent");
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    /**
     * Lấy danh sách tiến độ theo CustomerProgramID
     * @param customerProgramId ID của CustomerProgram
     * @return Danh sách Progress
     */
    public List<Progress> getProgressByCustomerProgramID(int customerProgramId) {
        List<Progress> list = new ArrayList<>();
        String sql = "SELECT ProgressID, CustomerProgramID, ProgressPercent, RecordedAt " +
                "FROM Progress WHERE CustomerProgramID = ? ORDER BY RecordedAt DESC";

        try (Connection conn = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerProgramId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Progress p = new Progress();
                    p.setProgressID(rs.getInt("ProgressID"));
                    p.setCustomerProgramID(rs.getInt("CustomerProgramID"));
                    p.setProgressPercent(rs.getDouble("ProgressPercent"));
                    Timestamp recordedAt = rs.getTimestamp("RecordedAt");
                    if (recordedAt != null) {
                        p.setRecordedAt(recordedAt.toLocalDateTime());
                    }
                    list.add(p);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Cập nhật tiến độ
     * @param p Đối tượng Progress
     */
    public void updateProgress(Progress p) {
        String sql = "UPDATE Progress SET ProgressPercent = ?, RecordedAt = ? WHERE ProgressID = ?";

        try (Connection conn = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, p.getProgressPercent());
            ps.setTimestamp(2, Timestamp.valueOf(p.getRecordedAt()));
            ps.setInt(3, p.getProgressID());
            ps.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lấy tiến độ theo ProgressID
     * @param id ID của tiến độ
     * @return Đối tượng Progress hoặc null nếu không tìm thấy
     */
    public Progress getProgressById(int id) {
        String sql = "SELECT ProgressID, CustomerProgramID, ProgressPercent, RecordedAt " +
                "FROM Progress WHERE ProgressID = ?";
        try (Connection conn = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Progress p = new Progress();
                    p.setProgressID(rs.getInt("ProgressID"));
                    p.setCustomerProgramID(rs.getInt("CustomerProgramID"));
                    p.setProgressPercent(rs.getDouble("ProgressPercent"));
                    Timestamp recordedAt = rs.getTimestamp("RecordedAt");
                    if (recordedAt != null) {
                        p.setRecordedAt(recordedAt.toLocalDateTime());
                    }
                    return p;
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
