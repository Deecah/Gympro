package dao;

import connectDB.ConnectDatabase; // Giả định lớp này đã tồn tại
import model.CustomerWorkoutSchedule;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class CustomerWorkoutScheduleDAO {

    /**
     * Thêm một lịch trình workout mới vào bảng CustomerWorkoutSchedule
     * @param schedule Đối tượng CustomerWorkoutSchedule chứa thông tin lịch trình
     * @return ScheduleID của bản ghi vừa thêm, hoặc -1 nếu thất bại
     */
    public int addSchedule(CustomerWorkoutSchedule schedule) {
        String sql = "INSERT INTO CustomerWorkoutSchedule (CustomerProgramID, ProgramDayID, WorkoutID, ScheduledDate, StartTime, EndTime, Status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, schedule.getCustomerProgramId());
            ps.setInt(2, schedule.getProgramDayId());
            ps.setInt(3, schedule.getWorkoutId());
            ps.setDate(4, java.sql.Date.valueOf(schedule.getScheduledDate()));
            ps.setTime(5, java.sql.Time.valueOf(schedule.getStartTime()));
            ps.setTime(6, java.sql.Time.valueOf(schedule.getEndTime()));
            ps.setString(7, schedule.getStatus());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1); // Trả về ScheduleID
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return -1; // Trả về -1 nếu có lỗi
    }

    /**
     * Lấy danh sách lịch trình workout theo CustomerProgramID
     * @param customerProgramId ID của chương trình khách hàng
     * @return Danh sách các đối tượng CustomerWorkoutSchedule
     */
    public List<CustomerWorkoutSchedule> getSchedulesByCustomerProgram(int customerProgramId) {
        List<CustomerWorkoutSchedule> schedules = new ArrayList<>();
        String sql = "SELECT * FROM CustomerWorkoutSchedule WHERE CustomerProgramID = ?";
        try (Connection conn = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerProgramId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CustomerWorkoutSchedule schedule = new CustomerWorkoutSchedule();
                    schedule.setScheduleId(rs.getInt("ScheduleID"));
                    schedule.setCustomerProgramId(rs.getInt("CustomerProgramID"));
                    schedule.setProgramDayId(rs.getInt("ProgramDayID"));
                    schedule.setWorkoutId(rs.getInt("WorkoutID"));
                    schedule.setScheduledDate(rs.getDate("ScheduledDate").toLocalDate());
                    schedule.setStartTime(rs.getTime("StartTime").toLocalTime());
                    schedule.setEndTime(rs.getTime("EndTime").toLocalTime());
                    schedule.setStatus(rs.getString("Status"));
                    schedules.add(schedule);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return schedules;
    }

    /**
     * Cập nhật trạng thái của một lịch trình workout
     * @param scheduleId ID của lịch trình cần cập nhật
     * @param status Trạng thái mới (pending, completed, missed)
     */
    public void updateStatus(int scheduleId, String status) {
        String sql = "UPDATE CustomerWorkoutSchedule SET Status = ? WHERE ScheduleID = ?";
        try (Connection conn = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, scheduleId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}