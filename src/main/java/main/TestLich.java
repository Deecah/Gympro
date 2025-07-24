package main;

import dao.WorkoutDAO;
import model.WorkoutSlotDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class TestLich {
    public static void main(String[] args) {
        int customerId = 4; 
        String role = "Customer"; 

        LocalDate startOfWeek = LocalDate.of(2025, 7, 21); // Thứ 2
        LocalDate endOfWeek = startOfWeek.plusDays(6);

        WorkoutDAO dao = new WorkoutDAO();
        Map<Integer, List<WorkoutSlotDTO>> slotMap = dao.getSlotSchedule(customerId, startOfWeek, endOfWeek, role);

        System.out.println("=== LỊCH TẬP CỦA KHÁCH " + customerId + " TUẦN: " + startOfWeek + " - " + endOfWeek + " ===");
        for (Map.Entry<Integer, List<WorkoutSlotDTO>> entry : slotMap.entrySet()) {
            int slotId = entry.getKey();
            List<WorkoutSlotDTO> list = entry.getValue();
            System.out.println("📌 SlotId: " + slotId);
            for (WorkoutSlotDTO w : list) {
                System.out.println("  ▶ " + w.getTitle()
                        + " (" + w.getStartStr() + "-" + w.getEndStr() + ")"
                        + " | " + w.getProgramName()
                        + " | Trainer/HV: " + w.getDisplayName());
            }
        }

        if (slotMap.isEmpty()) {
            System.out.println("⚠️ Không có lịch tập trong tuần này.");
        }
    }
}
