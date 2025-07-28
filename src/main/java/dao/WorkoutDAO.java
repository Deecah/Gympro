package dao;

import connectDB.ConnectDatabase;
import model.Workout;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.WorkoutSlotDTO;

public class WorkoutDAO {

    public int createWorkout(int dayId, String title, String notes, LocalTime startTime, LocalTime endTime) {
        String sql = "INSERT INTO Workout (DayID, Title, Notes, StartTime, EndTime) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = ConnectDatabase.getInstance().openConnection(); PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, dayId);
            ps.setString(2, title);
            ps.setString(3, notes);
            ps.setTime(4, startTime != null ? Time.valueOf(startTime) : null);
            ps.setTime(5, endTime != null ? Time.valueOf(endTime) : null);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (Exception e) {
            Logger.getLogger(WorkoutDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return -1;
    }

    public List<Workout> getWorkoutsByDayId(int dayId) {
        String sql = "SELECT * FROM Workout WHERE DayID = ?";
        List<Workout> list = new ArrayList<>();
        try (Connection con = ConnectDatabase.getInstance().openConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, dayId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(extractWorkout(rs));
                }
            }

        } catch (Exception e) {
            Logger.getLogger(WorkoutDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return list;
    }

    public int getDayIdByWorkoutId(int workoutId) {
        String sql = "SELECT DayID FROM Workout WHERE WorkoutID = ?";
        try (Connection con = ConnectDatabase.getInstance().openConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, workoutId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("DayID");
                }
            }
        } catch (Exception e) {
            Logger.getLogger(WorkoutDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return -1;
    }

    // ✅ Optional: lấy danh sách buổi tập của 1 ngày cụ thể
    public List<Workout> getWorkoutsByDate(int customerId, LocalDate date) {
        List<Workout> list = new ArrayList<>();
        String sql = "SELECT DATEADD(DAY, ((pw.WeekNumber - 1) * 7 + (pd.DayNumber - 1)), cp.StartDate) AS WorkoutDate, "
                + "w.WorkoutID, w.DayID, w.Title, w.Notes, w.StartTime, w.EndTime, w.CreatedAt "
                + "FROM CustomerProgram cp "
                + "JOIN Program p ON cp.ProgramID = p.ProgramID "
                + "JOIN ProgramWeek pw ON p.ProgramID = pw.ProgramID "
                + "JOIN ProgramDay pd ON pw.WeekID = pd.WeekID "
                + "JOIN Workout w ON pd.DayID = w.DayID "
                + "WHERE cp.CustomerID = ? "
                + "AND DATEADD(DAY, ((pw.WeekNumber - 1) * 7 + (pd.DayNumber - 1)), cp.StartDate) = ?";
        try (Connection con = ConnectDatabase.getInstance().openConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            ps.setDate(2, java.sql.Date.valueOf(date));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(extractWorkout(rs));
                }
            }
        } catch (Exception e) {
            Logger.getLogger(WorkoutDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return list;
    }

    // ✅ Trích xuất đối tượng Workout từ ResultSet
    private Workout extractWorkout(ResultSet rs) throws SQLException {
        Workout w = new Workout();
        w.setWorkoutID(rs.getInt("WorkoutID"));
        w.setDayID(rs.getInt("DayID"));
        w.setTitle(rs.getString("Title"));
        w.setNotes(rs.getString("Notes"));

        Time startTime = rs.getTime("StartTime");
        if (startTime != null) {
            LocalTime st = startTime.toLocalTime();
            w.setStartTime(st);
            w.setStartHour(st.getHour());
            w.setStartStr(st.format(DateTimeFormatter.ofPattern("HH:mm")));
        }

        Time endTime = rs.getTime("EndTime");
        if (endTime != null) {
            LocalTime et = endTime.toLocalTime();
            w.setEndTime(et);
            w.setEndStr(et.format(DateTimeFormatter.ofPattern("HH:mm")));
        }

        w.setCreatedAt(rs.getTimestamp("CreatedAt"));
        return w;
    }

    // ✅ Optional: tạo danh sách ngày trong tuần hiện tại
    public List<LocalDate> getCurrentWeekDates() {
        LocalDate today = LocalDate.now();
        LocalDate monday = today.minusDays(today.getDayOfWeek().getValue() - 1);
        List<LocalDate> week = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            week.add(monday.plusDays(i));
        }
        return week;
    }

    public Map<Integer, List<WorkoutSlotDTO>> getSlotSchedule(int customerId, LocalDate startOfWeek, LocalDate endOfWeek, String role) {
    Map<Integer, List<WorkoutSlotDTO>> map = new HashMap<>();

    String sql = "SELECT DATEADD(DAY, ((pw.WeekNumber - 1) * 7 + (pd.DayNumber - 1)), cp.StartDate) AS WorkoutDate, "
            + "w.WorkoutID, w.Title, w.StartTime, w.EndTime, "
            + "p.Name AS ProgramName, u.Name AS TrainerName "
            + "FROM CustomerProgram cp "
            + "JOIN Program p ON cp.ProgramID = p.ProgramID "
            + "JOIN Trainer t ON p.TrainerID = t.Id "
            + "JOIN Users u ON t.Id = u.Id "
            + "JOIN ProgramWeek pw ON p.ProgramID = pw.ProgramID "
            + "JOIN ProgramDay pd ON pw.WeekID = pd.WeekID "
            + "JOIN Workout w ON pd.DayID = w.DayID "
            + "WHERE cp.CustomerID = ?";

    try (Connection con = ConnectDatabase.getInstance().openConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, customerId); 

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                LocalDate date = rs.getDate("WorkoutDate").toLocalDate();

                if (date.isBefore(startOfWeek) || date.isAfter(endOfWeek)) {
                    continue;
                }

                LocalTime start = rs.getTime("StartTime").toLocalTime();
                int block;
                int hour = start.getHour();
                if (hour >= 6 && hour < 8) {
                    block = 1;
                } else if (hour >= 8 && hour < 10) {
                    block = 2;
                } else if (hour >= 10 && hour < 12) {
                    block = 3;
                } else if (hour >= 14 && hour < 16) {
                    block = 4;
                } else if (hour >= 16 && hour < 18) {
                    block = 5;
                } else if (hour >= 18 && hour < 20) {
                    block = 6;
                } else if (hour >= 20 && hour < 22) {
                    block = 7;
                } else {
                    block = -1;
                }
                if (block == -1) {
                    continue;
                }
                int dayIndex = date.getDayOfWeek().getValue() - 1; // 0: Monday, 6: Sunday
                int slotId = dayIndex * 10 + block;

                WorkoutSlotDTO dto = new WorkoutSlotDTO();
                dto.setSlotId(slotId);
                dto.setWorkoutId(rs.getInt("WorkoutID"));
                dto.setTitle(rs.getString("Title"));
                dto.setProgramName(rs.getString("ProgramName"));
                dto.setStartStr(start.format(DateTimeFormatter.ofPattern("HH:mm")));
                dto.setEndStr(rs.getTime("EndTime").toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")));
                dto.setDisplayName("Customer".equalsIgnoreCase(role)
                        ? rs.getString("TrainerName")
                        : "Học viên");

                map.computeIfAbsent(slotId, k -> new ArrayList<>()).add(dto);
            }
        }
    } catch (Exception e) {
        Logger.getLogger(WorkoutDAO.class.getName()).log(Level.SEVERE, null, e);
    }

    return map;
}

    public List<Workout> getWorkoutsUpToToday(int userId) {
        List<Workout> list = new ArrayList<>();
        String sql = "SELECT * FROM Workout WHERE UserID = ? AND Date <= ? ORDER BY Date DESC";
        try (Connection con = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setDate(2, java.sql.Date.valueOf(java.time.LocalDate.now()));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(extractWorkout(rs));
                }
            }
        } catch (Exception e) {
            Logger.getLogger(WorkoutDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return list;
    }

    public Workout getWorkoutById(int workoutId) {
        String sql = "SELECT w.*, p.Name AS ProgramName FROM Workout w " +
                    "LEFT JOIN ProgramDay pd ON w.DayID = pd.DayID " +
                    "LEFT JOIN ProgramWeek pw ON pd.WeekID = pw.WeekID " +
                    "LEFT JOIN Program p ON pw.ProgramID = p.ProgramID " +
                    "WHERE w.WorkoutID = ?";
        try (Connection con = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, workoutId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Workout workout = extractWorkout(rs);
                    workout.setProgramName(rs.getString("ProgramName"));
                    return workout;
                }
            }
        } catch (Exception e) {
            Logger.getLogger(WorkoutDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    public boolean updateWorkout(int workoutId, String title, String notes, LocalTime startTime, LocalTime endTime) {
        String sql = "UPDATE Workout SET Title = ?, Notes = ?, StartTime = ?, EndTime = ? WHERE WorkoutID = ?";
        try (Connection con = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, title);
            ps.setString(2, notes);
            ps.setTime(3, startTime != null ? Time.valueOf(startTime) : null);
            ps.setTime(4, endTime != null ? Time.valueOf(endTime) : null);
            ps.setInt(5, workoutId);
            
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            Logger.getLogger(WorkoutDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return false;
    }

    public boolean deleteWorkout(int workoutId) {
        // First delete all exercises in this workout
        String deleteExercisesSql = "DELETE FROM Exercise WHERE WorkoutID = ?";
        String deleteWorkoutSql = "DELETE FROM Workout WHERE WorkoutID = ?";
        
        try (Connection con = ConnectDatabase.getInstance().openConnection()) {
            con.setAutoCommit(false);
            try {
                // Delete exercises first (cascade delete)
                try (PreparedStatement ps = con.prepareStatement(deleteExercisesSql)) {
                    ps.setInt(1, workoutId);
                    ps.executeUpdate();
                }
                
                // Then delete the workout
                try (PreparedStatement ps = con.prepareStatement(deleteWorkoutSql)) {
                    ps.setInt(1, workoutId);
                    int rows = ps.executeUpdate();
                    con.commit();
                    return rows > 0;
                }
            } catch (Exception e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        } catch (Exception e) {
            Logger.getLogger(WorkoutDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return false;
    }

//    public List<Workout> getWorkoutsByCustomerId(int customerId) {
//        List<Workout> list = new ArrayList<>();
//
//        String sql
//                = "SELECT w.*, p.Name AS ProgramName "
//                + "FROM CustomerProgram cp "
//                + "JOIN Program p ON cp.ProgramID = p.ProgramID "
//                + "JOIN ProgramWeek pw ON p.ProgramID = pw.ProgramID "
//                + "JOIN ProgramDay pd ON pw.WeekID = pd.WeekID "
//                + "JOIN Workout w ON pd.DayID = w.DayID "
//                + "WHERE cp.CustomerID = ? "
//                + "ORDER BY w.CreatedAt DESC";
//
//        try (Connection con = ConnectDatabase.getInstance().openConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
//
//            ps.setInt(1, customerId);
//
//            try (ResultSet rs = ps.executeQuery()) {
//                while (rs.next()) {
//                    Workout w = extractWorkout(rs);
//                    w.setProgramName(rs.getString("ProgramName"));
//                    list.add(w);
//                }
//            }
//
//        } catch (Exception e) {
//            Logger.getLogger(WorkoutDAO.class.getName()).log(Level.SEVERE, null, e);
//        }
//
//        return list;
//    }

}
