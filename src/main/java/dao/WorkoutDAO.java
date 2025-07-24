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
import model.CustomerProgram;
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

    public Map<LocalDate, List<Workout>> getScheduleMap(List<CustomerProgram> programs, LocalDate startOfWeek, LocalDate endOfWeek) {
        Map<LocalDate, List<Workout>> map = new TreeMap<>();
        String sql = "SELECT DATEADD(DAY, ((pw.WeekNumber - 1) * 7 + (pd.DayNumber - 1)), ?) AS WorkoutDate, "
                + "w.WorkoutID, w.DayID, w.Title, w.Notes, w.StartTime, w.EndTime, w.CreatedAt, p.Name AS ProgramName "
                + "FROM Program p "
                + "JOIN ProgramWeek pw ON p.ProgramID = pw.ProgramID "
                + "JOIN ProgramDay pd ON pw.WeekID = pd.WeekID "
                + "JOIN Workout w ON pd.DayID = w.DayID "
                + "WHERE p.ProgramID = ?";

        try (Connection con = ConnectDatabase.getInstance().openConnection()) {
            for (CustomerProgram cp : programs) {
                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setDate(1, java.sql.Date.valueOf(cp.getStartDate()));
                    ps.setInt(2, cp.getProgramId());

                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            LocalDate date = rs.getDate("WorkoutDate").toLocalDate();
                            if (!date.isBefore(startOfWeek) && !date.isAfter(endOfWeek)) {
                                Workout w = extractWorkout(rs);
                                w.setProgramName(rs.getString("ProgramName"));
                                map.computeIfAbsent(date, k -> new ArrayList<>()).add(w);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            Logger.getLogger(WorkoutDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return map;
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
            + "w.Title, w.StartTime, w.EndTime, "
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

        ps.setInt(1, customerId);  // ✅ đúng duy nhất 1 tham số

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                LocalDate date = rs.getDate("WorkoutDate").toLocalDate();

                // ❗ Bỏ qua nếu không nằm trong tuần đã chọn
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


}
