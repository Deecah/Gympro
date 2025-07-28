package dao;

import connectDB.ConnectDatabase;
import model.CustomerProgram;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomerProgramDAO {

    public int assignProgramToCustomer(int programId, int customerId, LocalDate startDate) {
        // Tính toán ngày bắt đầu hợp lý để tránh các buổi tập trong quá khứ
        LocalDate adjustedStartDate = calculateAdjustedStartDate(startDate);
        
        String sql = "INSERT INTO CustomerProgram (ProgramID, CustomerID, StartDate) VALUES (?, ?, ?)";
        try (Connection con = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, programId);
            ps.setInt(2, customerId);
            ps.setDate(3, Date.valueOf(adjustedStartDate));
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (Exception e) {
            Logger.getLogger(CustomerProgramDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return -1;
    }
    
    /**
     * Tính toán ngày bắt đầu hợp lý để tránh các buổi tập trong quá khứ
     * Nếu ngày được chọn là chủ nhật nhưng chương trình có buổi tập vào thứ 2, thứ 3
     * thì sẽ điều chỉnh để buổi tập đầu tiên không rơi vào quá khứ
     */
    private LocalDate calculateAdjustedStartDate(LocalDate requestedStartDate) {
        LocalDate currentDate = LocalDate.now();
        
        // Nếu ngày yêu cầu đã trong quá khứ, bắt đầu từ ngày mai
        if (requestedStartDate.isBefore(currentDate)) {
            return currentDate.plusDays(1);
        }
        
        // Nếu ngày yêu cầu là hôm nay, kiểm tra xem có phải là ngày cuối tuần không
        // Nếu là chủ nhật và đã muộn, bắt đầu từ thứ 2 tuần sau
        if (requestedStartDate.equals(currentDate)) {
            int dayOfWeek = currentDate.getDayOfWeek().getValue(); // 1=Monday, 7=Sunday
            if (dayOfWeek == 7) { // Chủ nhật
                // Nếu đã muộn trong ngày chủ nhật, bắt đầu từ thứ 2 tuần sau
                return currentDate.plusDays(1); // Thứ 2 tuần sau
            }
        }
        
        return requestedStartDate;
    }

    // ✅ Dùng khi chỉ muốn lấy chương trình mới nhất (nếu cần)
    public CustomerProgram getLatestProgramByCustomer(int customerId) {
        String sql = "SELECT TOP 1 * FROM CustomerProgram WHERE CustomerID = ? ORDER BY AssignedAt DESC";
        try (Connection con = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    CustomerProgram cp = new CustomerProgram();
                    cp.setId(rs.getInt("Id"));
                    cp.setProgramId(rs.getInt("ProgramID"));
                    cp.setCustomerId(rs.getInt("CustomerID"));
                    cp.setStartDate(rs.getDate("StartDate").toLocalDate());
                    cp.setAssignedAt(rs.getTimestamp("AssignedAt").toLocalDateTime());
                    return cp;
                }
            }

        } catch (Exception e) {
            Logger.getLogger(CustomerProgramDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    // ✅ Dùng cho lịch tập – lấy tất cả chương trình đã gán cho khách hàng
    public List<CustomerProgram> getActiveProgramsByCustomer(int customerId) {
        List<CustomerProgram> list = new ArrayList<>();
        String sql = "SELECT * FROM CustomerProgram WHERE CustomerID = ? AND StartDate IS NOT NULL";
        try (Connection con = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CustomerProgram cp = new CustomerProgram();
                    cp.setId(rs.getInt("Id"));
                    cp.setProgramId(rs.getInt("ProgramID"));
                    cp.setCustomerId(rs.getInt("CustomerID"));
                    cp.setStartDate(rs.getDate("StartDate").toLocalDate());
                    cp.setAssignedAt(rs.getTimestamp("AssignedAt").toLocalDateTime());
                    list.add(cp);
                }
            }

        } catch (Exception e) {
            Logger.getLogger(CustomerProgramDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return list;
    }

    public boolean isProgramAlreadyAssigned(int customerId, int programId) {
        String sql = "SELECT 1 FROM CustomerProgram WHERE CustomerID = ? AND ProgramID = ?";
        try (Connection con = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, customerId);
            ps.setInt(2, programId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (Exception e) {
            Logger.getLogger(CustomerProgramDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return false;
    }

    public List<CustomerProgram> getAllAssignedPrograms() {
        List<CustomerProgram> list = new ArrayList<>();
        String sql = "SELECT * FROM CustomerProgram ORDER BY AssignedAt DESC";
        try (Connection con = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                CustomerProgram cp = new CustomerProgram();
                cp.setId(rs.getInt("Id"));
                cp.setProgramId(rs.getInt("ProgramID"));
                cp.setCustomerId(rs.getInt("CustomerID"));
                cp.setStartDate(rs.getDate("StartDate").toLocalDate());
                cp.setAssignedAt(rs.getTimestamp("AssignedAt").toLocalDateTime());
                list.add(cp);
            }

        } catch (Exception e) {
            Logger.getLogger(CustomerProgramDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return list;
    }
}
