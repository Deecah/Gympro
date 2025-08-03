package dao;

import connectDB.ConnectDatabase;
import model.CustomerWorkoutSchedule;
import model.Exercise;
import model.ExerciseProgram;
import model.Workout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TimetableDAO {

    /**
     * Lấy danh sách lịch trình workout cho một customerProgramId trong một tuần cụ thể.
     * @param customerProgramId ID của chương trình khách hàng
     * @param weekStartDate Ngày bắt đầu của tuần (định dạng yyyy-MM-dd)
     * @return Danh sách các đối tượng CustomerWorkoutSchedule
     */
    public List<CustomerWorkoutSchedule> getSchedulesForWeek(int customerProgramId, String weekStartDate) {
        List<CustomerWorkoutSchedule> schedules = new ArrayList<>();
        String sql = "SELECT * FROM CustomerWorkoutSchedule " +
                "WHERE CustomerProgramID = ? AND ScheduledDate BETWEEN ? AND DATEADD(day, 6, ?)";
        try (Connection conn = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerProgramId);
            ps.setString(2, weekStartDate);
            ps.setString(3, weekStartDate);
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
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return schedules;
    }

    /**
     * Lấy danh sách ExerciseProgram theo ProgramID
     * @param programId ID của chương trình
     * @return Danh sách các đối tượng ExerciseProgram
     */
    public List<ExerciseProgram> getExerciseProgramsByProgramId(int programId) {
        List<ExerciseProgram> exercisePrograms = new ArrayList<>();
        String sql = "SELECT * FROM ExerciseProgram WHERE ProgramID = ?";
        try (Connection conn = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, programId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ExerciseProgram ep = new ExerciseProgram();
                    ep.setExerciseProgramID(rs.getInt("ExerciseProgramID"));
                    ep.setExerciseLibraryID(rs.getInt("ExerciseLibraryID"));
                    ep.setTrainerID(rs.getInt("TrainerID"));
                    ep.setProgramID(rs.getInt("ProgramID"));
                    exercisePrograms.add(ep);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return exercisePrograms;
    }

    /**
     * Thêm một workout mới, các bài tập từ ExerciseProgram, và liên kết với lịch trình của khách hàng.
     * @param workout Đối tượng Workout chứa thông tin workout
     * @param schedule Đối tượng CustomerWorkoutSchedule chứa thông tin lịch trình
     * @param exerciseProgramIds Danh sách ID của ExerciseProgram
     * @return ScheduleID của bản ghi vừa thêm, hoặc -1 nếu thất bại
     */
    public int addWorkoutAndSchedule(Workout workout, CustomerWorkoutSchedule schedule, List<Integer> exerciseProgramIds) {
        Connection conn = null;
        try {
            conn = ConnectDatabase.getInstance().openConnection();
            conn.setAutoCommit(false); // Bắt đầu transaction

            // Thêm workout
            String workoutSql = "INSERT INTO Workout (DayID, StartTime, EndTime, Title, Notes, TrainerID) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement workoutPs = conn.prepareStatement(workoutSql, Statement.RETURN_GENERATED_KEYS)) {
                workoutPs.setInt(1, workout.getDayId());
                workoutPs.setTime(2, workout.getStartTime());
                workoutPs.setTime(3, workout.getEndTime());
                workoutPs.setString(4, workout.getTitle());
                workoutPs.setString(5, workout.getNotes());
                workoutPs.setInt(6, workout.getTrainerId());
                workoutPs.executeUpdate();
                try (ResultSet rs = workoutPs.getGeneratedKeys()) {
                    if (rs.next()) {
                        int workoutId = rs.getInt(1);
                        schedule.setWorkoutId(workoutId);

                        // Thêm các bài tập từ ExerciseProgram
                        String exerciseSql = "INSERT INTO Exercise (WorkoutID, ExerciseLibraryID) VALUES (?, ?)";
                        try (PreparedStatement exercisePs = conn.prepareStatement(exerciseSql)) {
                            for (Integer exerciseProgramId : exerciseProgramIds) {
                                int exerciseLibraryId = getExerciseLibraryIdByProgramId(exerciseProgramId, conn);
                                if (exerciseLibraryId != -1) {
                                    exercisePs.setInt(1, workoutId);
                                    exercisePs.setInt(2, exerciseLibraryId);
                                    exercisePs.executeUpdate();
                                }
                            }
                        }

                        // Thêm schedule
                        String scheduleSql = "INSERT INTO CustomerWorkoutSchedule (CustomerProgramID, ProgramDayID, WorkoutID, ScheduledDate, StartTime, EndTime, Status) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?)";
                        try (PreparedStatement schedulePs = conn.prepareStatement(scheduleSql, Statement.RETURN_GENERATED_KEYS)) {
                            schedulePs.setInt(1, schedule.getCustomerProgramId());
                            schedulePs.setInt(2, schedule.getProgramDayId());
                            schedulePs.setInt(3, schedule.getWorkoutId());
                            schedulePs.setDate(4, java.sql.Date.valueOf(schedule.getScheduledDate()));
                            schedulePs.setTime(5, java.sql.Time.valueOf(schedule.getStartTime()));
                            schedulePs.setTime(6, java.sql.Time.valueOf(schedule.getEndTime()));
                            schedulePs.setString(7, schedule.getStatus());
                            schedulePs.executeUpdate();
                            try (ResultSet scheduleRs = schedulePs.getGeneratedKeys()) {
                                if (scheduleRs.next()) {
                                    conn.commit();
                                    return scheduleRs.getInt(1);
                                }
                            }
                        }
                    }
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return -1;
    }

    /**
     * Lấy ExerciseLibraryID từ ExerciseProgramID
     * @param exerciseProgramId ID của ExerciseProgram
     * @param conn Kết nối cơ sở dữ liệu
     * @return ExerciseLibraryID hoặc -1 nếu không tìm thấy
     */
    private int getExerciseLibraryIdByProgramId(int exerciseProgramId, Connection conn) throws SQLException {
        String sql = "SELECT ExerciseLibraryID FROM ExerciseProgram WHERE ExerciseProgramID = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, exerciseProgramId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("ExerciseLibraryID");
                }
            }
        }
        return -1;
    }

    /**
     * Cập nhật trạng thái của một lịch trình workout.
     * @param scheduleId ID của lịch trình
     * @param status Trạng thái mới
     */
    public void updateScheduleStatus(int scheduleId, String status) {
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