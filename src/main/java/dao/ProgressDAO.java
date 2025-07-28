package dao;

import connectDB.ConnectDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.CustomerProgressDTO;
import model.WorkoutPostDTO;

public class ProgressDAO {

    public boolean markWorkoutCompleted(int userId, int workoutId, String notes) {
        String sql = "MERGE Progress AS target " +
                    "USING (SELECT ? AS UserID, ? AS WorkoutID, ? AS Notes) AS source " +
                    "ON (target.UserID = source.UserID AND target.WorkoutID = source.WorkoutID) " +
                    "WHEN MATCHED THEN " +
                    "    UPDATE SET Completed = 1, CompletedAt = GETDATE(), Notes = source.Notes " +
                    "WHEN NOT MATCHED THEN " +
                    "    INSERT (UserID, WorkoutID, Completed, CompletedAt, Notes) " +
                    "    VALUES (source.UserID, source.WorkoutID, 1, GETDATE(), source.Notes);";
        
        try (Connection con = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, workoutId);
            ps.setString(3, notes);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) { 
            e.printStackTrace(); 
            return false;
        }
    }

    public boolean isWorkoutCompleted(int userId, int workoutId) {
        String sql = "SELECT Completed FROM Progress WHERE UserID = ? AND WorkoutID = ? AND Completed = 1";
        try (Connection con = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, workoutId);
            ResultSet rs = ps.executeQuery();
            return rs.next(); // Returns true if record exists and Completed = 1
        } catch (Exception e) { 
            e.printStackTrace(); 
            return false;
        }
    }
    
    /**
     * Lấy tổng số bài tập cho khách hàng theo program cụ thể
     */
    public int getTotalWorkoutsForCustomerByProgram(int customerId, int programId) {
        String sql = "SELECT COUNT(DISTINCT w.WorkoutID) as total " +
                     "FROM Workout w " +
                     "INNER JOIN ProgramDay pd ON w.DayID = pd.DayID " +
                     "INNER JOIN ProgramWeek pw ON pd.WeekID = pw.WeekID " +
                     "INNER JOIN Program p ON pw.ProgramID = p.ProgramID " +
                     "INNER JOIN CustomerProgram cp ON p.ProgramID = cp.ProgramID " +
                     "WHERE cp.CustomerID = ? AND cp.ProgramID = ?";
        
        try (Connection con = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, customerId);
            ps.setInt(2, programId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("total");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Lấy số bài tập đã hoàn thành cho khách hàng theo program cụ thể
     */
    public int getCompletedWorkoutsForCustomerByProgram(int customerId, int programId) {
        String sql = "SELECT COUNT(DISTINCT p.WorkoutID) as completed " +
                     "FROM Progress p " +
                     "INNER JOIN Workout w ON p.WorkoutID = w.WorkoutID " +
                     "INNER JOIN ProgramDay pd ON w.DayID = pd.DayID " +
                     "INNER JOIN ProgramWeek pw ON pd.WeekID = pw.WeekID " +
                     "INNER JOIN Program prog ON pw.ProgramID = prog.ProgramID " +
                     "WHERE p.UserID = ? AND p.Completed = 1 AND prog.ProgramID = ?";
        
        try (Connection con = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, customerId);
            ps.setInt(2, programId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("completed");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Lấy thống kê tổng số bài tập theo từng chương trình cho khách hàng
     */
    public List<CustomerProgressDTO> getProgramProgressStats(int customerId) {
        List<CustomerProgressDTO> progressStats = new ArrayList<>();
        String sql = "SELECT " +
                     "    p.ProgramID, " +
                     "    p.Name AS ProgramName, " +
                     "    COUNT(wi.WorkoutID) AS TotalWorkouts " +
                     "FROM CustomerProgram cp " +
                     "JOIN Program p ON cp.ProgramID = p.ProgramID " +
                     "JOIN ProgramWeek pw ON p.ProgramID = pw.ProgramID " +
                     "JOIN ProgramDay pd ON pw.WeekID = pd.WeekID " +
                     "JOIN Workout wi ON pd.DayID = wi.DayID " +
                     "WHERE cp.CustomerID = ? " +
                     "GROUP BY p.ProgramID, p.Name " +
                     "ORDER BY MAX(wi.CreatedAt) DESC";

        try (Connection con = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                CustomerProgressDTO dto = new CustomerProgressDTO();
                dto.setProgramId(rs.getInt("ProgramID"));
                dto.setProgramName(rs.getString("ProgramName"));
                dto.setTotalWorkouts(rs.getInt("TotalWorkouts"));
                
                // Tính số bài tập đã hoàn thành cho chương trình này
                int completedWorkouts = getCompletedWorkoutsForCustomerByProgram(customerId, dto.getProgramId());
                dto.setCompletedWorkouts(completedWorkouts);
                
                // Tính phần trăm hoàn thành
                if (dto.getTotalWorkouts() > 0) {
                    double progressPercent = (double) completedWorkouts / dto.getTotalWorkouts() * 100;
                    dto.setProgressPercent(Math.round(progressPercent * 100.0) / 100.0);
                } else {
                    dto.setProgressPercent(0.0);
                }
                
                progressStats.add(dto);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return progressStats;
    }
    
    /**
     * Lấy danh sách bài tập theo chương trình với trạng thái hoàn thành
     */
    public List<WorkoutPostDTO> getWorkoutsByProgramWithStatus(int customerId, int programId) {
        List<WorkoutPostDTO> workouts = new ArrayList<>();
        String sql = "SELECT " +
                     "    w.WorkoutID, " +
                     "    w.WorkoutName, " +
                     "    w.Description, " +
                     "    w.ScheduledTime, " +
                     "    w.CreatedAt, " +
                     "    pd.DayName, " +
                     "    pw.WeekNumber, " +
                     "    CASE WHEN pr.Completed = 1 THEN 1 ELSE 0 END AS IsCompleted, " +
                     "    pr.CompletedAt, " +
                     "    pr.Notes " +
                     "FROM Workout w " +
                     "INNER JOIN ProgramDay pd ON w.DayID = pd.DayID " +
                     "INNER JOIN ProgramWeek pw ON pd.WeekID = pw.WeekID " +
                     "INNER JOIN Program p ON pw.ProgramID = p.ProgramID " +
                     "INNER JOIN CustomerProgram cp ON p.ProgramID = cp.ProgramID " +
                     "LEFT JOIN Progress pr ON w.WorkoutID = pr.WorkoutID AND pr.UserID = ? " +
                     "WHERE cp.CustomerID = ? AND p.ProgramID = ? " +
                     "ORDER BY pw.WeekNumber, pd.DayNumber, w.ScheduledTime";

        try (Connection con = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, customerId);
            ps.setInt(2, customerId);
            ps.setInt(3, programId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                WorkoutPostDTO workout = new WorkoutPostDTO();
                workout.setWorkoutId(rs.getInt("WorkoutID"));
                workout.setWorkoutName(rs.getString("WorkoutName"));
                workout.setDescription(rs.getString("Description"));
                workout.setDayName(rs.getString("DayName"));
                workout.setWeekNumber(rs.getInt("WeekNumber"));
                workout.setCompleted(rs.getBoolean("IsCompleted"));
                
                Timestamp scheduledTime = rs.getTimestamp("ScheduledTime");
                if (scheduledTime != null) {
                    workout.setScheduledTime(scheduledTime.toLocalDateTime());
                }
                
                Timestamp completedAt = rs.getTimestamp("CompletedAt");
                if (completedAt != null) {
                    workout.setCompletedAt(completedAt.toLocalDateTime());
                }
                
                workout.setNotes(rs.getString("Notes"));
                workouts.add(workout);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return workouts;
    }
}
