package main;

import dao.ExerciseLibraryDAO;
import model.ExerciseLibrary;

import java.util.List;

public class TestExerciseLibraryDAO {

    public static void main(String[] args) {
        ExerciseLibraryDAO dao = new ExerciseLibraryDAO();

        System.out.println("Fetching all exercises from database...");
        List<ExerciseLibrary> exercises = dao.getAllExercises();

        if (exercises.isEmpty()) {
            System.out.println("No exercises found!");
        } else {
            for (ExerciseLibrary ex : exercises) {
                System.out.println("----------------------------");
                System.out.println("ID: " + ex.getExerciseID());
                System.out.println("Name: " + ex.getName());
                System.out.println("Description: " + ex.getDescription());
                System.out.println("Muscle Group: " + ex.getMuscleGroup());
                System.out.println("Equipment: " + ex.getEquipment());
                System.out.println("Video URL: " + ex.getVideoURL());
            }
        }
    }
}
