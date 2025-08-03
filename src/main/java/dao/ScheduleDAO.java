package dao;

import connectDB.ConnectDatabase;
import model.Slot;
import model.Exercise;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ScheduleDAO {
    
    public static Slot getSlotById(int slotId) {
        Slot slot = null;
        try (Connection conn = ConnectDatabase.getInstance().openConnection()) {
            String sql = "SELECT * FROM Slots WHERE slot_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, slotId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                slot = new Slot();
                slot.setSlotId(slotId);
                slot.setDay(rs.getDate("day").toLocalDate());
                slot.setStartTime(rs.getTime("start_time").toLocalTime());
                slot.setEndTime(rs.getTime("end_time").toLocalTime());
                slot.setDuration(rs.getInt("duration"));

                // Lấy exercises bằng connection đã có
                slot.setExercises((ArrayList<Exercise>) getExercisesBySlotId(slotId, conn));
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return slot;
    }

    public static List<Slot> getSlotsByCustomerId(int customerId) {
        List<Slot> slots = new ArrayList<>();
        try (Connection conn = ConnectDatabase.getInstance().openConnection()) {
            String sql = "SELECT * FROM Slots WHERE customer_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Slot slot = new Slot();
                int slotId = rs.getInt("slot_id");
                slot.setSlotId(slotId);
                slot.setDay(rs.getDate("day").toLocalDate());
                slot.setStartTime(rs.getTime("start_time").toLocalTime());
                slot.setEndTime(rs.getTime("end_time").toLocalTime());
                slot.setDuration(rs.getInt("duration"));

                // Gọi hàm với connection đã có
                slot.setExercises((ArrayList<Exercise>) getExercisesBySlotId(slotId, conn));
                slots.add(slot);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return slots;
    }

    public static List<Exercise> getExercisesBySlotId(int slotId, Connection conn) {
        List<Exercise> exercises = new ArrayList<>();
        String sql = "SELECT * FROM Exercises WHERE slot_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, slotId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Exercise ex = new Exercise();
                ex.setExerciseID(rs.getInt("exercise_id"));
                exercises.add(ex);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exercises;
    }
}
