package main;

import dao.ProgramDAO;
import model.Program;

import java.util.List;

public class TestProgramDAO {
    public static void main(String[] args) {
        int trainerId = 1; // ID của trainer bạn muốn test

        ProgramDAO dao = new ProgramDAO();
        List<Program> programs = dao.getAllProgramsByTrainer(trainerId);

        if (programs == null || programs.isEmpty()) {
            System.out.println("❌ No programs found for TrainerID: " + trainerId);
        } else {
            System.out.println("✅ Programs for TrainerID: " + trainerId);
            for (Program p : programs) {
                System.out.println("ID: " + p.getProgramId());
                System.out.println("Name: " + p.getName());
                System.out.println("Description: " + p.getDescription());
                System.out.println("PackageID: " + p.getPackageId());
                System.out.println("---------------------------");
            }
        }
    }
}
