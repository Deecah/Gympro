package dao;

import connectDB.ConnectDatabase;
import model.CustomerProgram;
import model.CustomerProgramDTO;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CustomerProgramDAO {

    /**
     * Lấy danh sách chương trình của khách hàng theo TrainerID
     * @param trainerId ID của huấn luyện viên
     * @return Danh sách CustomerProgramDTO
     */
    public List<CustomerProgramDTO> getCustomerProgramsByTrainerId(int trainerId) {
        List<CustomerProgramDTO> customerPrograms = new ArrayList<>();
        String sql = "SELECT cp.Id, cp.CustomerID, cp.ProgramID, cp.StartDate, cp.EndDate, cp.AssignedAt, u.Name AS CustomerName, u.AvatarUrl, p.Name AS ProgramName, cws.ScheduleID " +
                "FROM CustomerProgram cp " +
                "JOIN Users u ON cp.CustomerID = u.Id " +
                "JOIN Program p ON cp.ProgramID = p.ProgramID " +
                "LEFT JOIN CustomerWorkoutSchedule cws ON cp.Id = cws.CustomerProgramID " +
                "WHERE p.TrainerID = ?";
        try (Connection conn = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, trainerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CustomerProgramDTO cp = new CustomerProgramDTO();
                    cp.setId(rs.getInt("Id"));
                    cp.setCustomerId(rs.getInt("CustomerID"));
                    cp.setProgramId(rs.getInt("ProgramID"));
                    cp.setAssignedAt(rs.getTimestamp("AssignedAt").toLocalDateTime());
                    cp.setCustomerName(rs.getString("CustomerName"));
                    cp.setProgramName(rs.getString("ProgramName"));
                    cp.setAvatarUrl(rs.getString("AvatarUrl"));
                    cp.setStartDate(rs.getDate("StartDate"));
                    cp.setEndDate(rs.getDate("EndDate"));
                    cp.setScheduleId(rs.getInt("ScheduleID"));
                    customerPrograms.add(cp);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return customerPrograms;
    }

    /**
     * Gán chương trình cho khách hàng
     * @param programId ID của chương trình
     * @param customerId ID của khách hàng
     * @param startDate Ngày bắt đầu
     * @return CustomerProgramID của bản ghi vừa thêm, hoặc -1 nếu thất bại
     */
    public int assignProgramToCustomer(int programId, int customerId, Date startDate, Date endDate) {
        String sql = "INSERT INTO CustomerProgram (ProgramID, CustomerID, StartDate, EndDate) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, programId);
            ps.setInt(2, customerId);
            ps.setDate(3,startDate);
            ps.setDate(4, endDate);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Tính toán ngày bắt đầu hợp lý để tránh các buổi tập trong quá khứ
     * @param requestedStartDate Ngày yêu cầu
     * @return Ngày bắt đầu đã điều chỉnh
     */

    /**
     * Lấy chương trình mới nhất của khách hàng
     * @param customerId ID của khách hàng
     * @return Đối tượng CustomerProgram hoặc null nếu không tìm thấy
     */
    public CustomerProgram getLatestProgramByCustomer(int customerId) {
        String sql = "SELECT TOP 1 * FROM CustomerProgram WHERE CustomerID = ? ORDER BY AssignedAt DESC";
        try (Connection conn = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    CustomerProgram cp = new CustomerProgram();
                    cp.setId(rs.getInt("Id"));
                    cp.setProgramId(rs.getInt("ProgramID"));
                    cp.setCustomerId(rs.getInt("CustomerID"));
                    cp.setStartDate(rs.getDate("StartDate"));
                    cp.setEndDate(rs.getDate("EndDate"));
                    cp.setAssignedAt(rs.getTimestamp("AssignedAt").toLocalDateTime());
                    return cp;
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Lấy danh sách chương trình hoạt động của khách hàng
     * @param customerId ID của khách hàng
     * @return Danh sách CustomerProgram
     */
    public List<CustomerProgram> getActiveProgramsByCustomer(int customerId) {
        List<CustomerProgram> list = new ArrayList<>();
        String sql = "SELECT * FROM CustomerProgram WHERE CustomerID = ? AND StartDate IS NOT NULL";
        try (Connection conn = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CustomerProgram cp = new CustomerProgram();
                    cp.setId(rs.getInt("Id"));
                    cp.setProgramId(rs.getInt("ProgramID"));
                    cp.setCustomerId(rs.getInt("CustomerID"));
                    cp.setStartDate(rs.getDate("StartDate"));
                    cp.setEndDate(rs.getDate("EndDate"));
                    cp.setAssignedAt(rs.getTimestamp("AssignedAt").toLocalDateTime());
                    list.add(cp);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Kiểm tra xem chương trình đã được gán cho khách hàng chưa
     * @param customerId ID của khách hàng
     * @param programId ID của chương trình
     * @return true nếu đã gán, false nếu chưa
     */
    public boolean isProgramAlreadyAssigned(int customerId, int programId) {
        String sql = "SELECT 1 FROM CustomerProgram WHERE CustomerID = ? AND ProgramID = ?";
        try (Connection conn = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            ps.setInt(2, programId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Lấy tất cả chương trình đã gán
     * @return Danh sách CustomerProgram
     */
    public List<CustomerProgram> getAllAssignedPrograms() {
        List<CustomerProgram> list = new ArrayList<>();
        String sql = "SELECT * FROM CustomerProgram ORDER BY AssignedAt DESC";
        try (Connection conn = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                CustomerProgram cp = new CustomerProgram();
                cp.setId(rs.getInt("Id"));
                cp.setProgramId(rs.getInt("ProgramID"));
                cp.setCustomerId(rs.getInt("CustomerID"));
                cp.setStartDate(rs.getDate("StartDate"));
                cp.setEndDate(rs.getDate("EndDate"));
                cp.setAssignedAt(rs.getTimestamp("AssignedAt").toLocalDateTime());
                list.add(cp);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }
}