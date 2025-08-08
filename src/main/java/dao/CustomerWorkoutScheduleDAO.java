package dao;

import connectDB.ConnectDatabase;
import model.CustomerWorkoutSchedule;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerWorkoutScheduleDAO {

    public int addSchedule(CustomerWorkoutSchedule schedule) {
        String sql = "INSERT INTO CustomerWorkoutSchedule (CustomerProgramID, StartAt, EndAt, Status) " +
                "VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, schedule.getCustomerProgramId());
            ps.setDate(2, schedule.getStartAt());
            ps.setDate(3, schedule.getEndAt());
            ps.setString(4, schedule.getStatus());
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

    public List<CustomerWorkoutSchedule> getSchedulesByCustomerProgram(int customerProgramId) {
        List<CustomerWorkoutSchedule> schedules = new ArrayList<>();
        String sql = "SELECT cws.*, p.Name AS ProgramName, u.Name AS CustomerName, cp.ProgramID " +
                "FROM CustomerWorkoutSchedule cws " +
                "JOIN CustomerProgram cp ON cws.CustomerProgramID = cp.Id " +
                "JOIN Program p ON cp.ProgramID = p.ProgramID " +
                "JOIN Users u ON cp.CustomerID = u.Id " +
                "WHERE cws.CustomerProgramID = ?";
        try (Connection conn = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerProgramId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CustomerWorkoutSchedule schedule = new CustomerWorkoutSchedule();
                    schedule.setScheduleId(rs.getInt("ScheduleID"));
                    schedule.setCustomerProgramId(rs.getInt("CustomerProgramID"));
                    schedule.setProgramId(rs.getInt("ProgramID"));
                    schedule.setStartAt(rs.getDate("StartAt"));
                    schedule.setEndAt(rs.getDate("EndAt"));
                    schedule.setStatus(rs.getString("Status"));
                    schedule.setProgramName(rs.getString("ProgramName"));
                    schedule.setCustomerName(rs.getString("CustomerName"));
                    schedules.add(schedule);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return schedules;
    }

    public void updateStatus(int scheduleId, String status) {
        String sql = "UPDATE CustomerWorkoutSchedule SET Status = ? WHERE ScheduleID = ?";
        try (Connection conn = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, scheduleId);
            ps.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}