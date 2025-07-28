///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package main;
//
//import dao.WorkoutDAO;
//import java.util.List;
//import model.Workout;
//
//public class TestWorkoutByCustomer {
//    public static void main(String[] args) {
//        WorkoutDAO dao = new WorkoutDAO();
//        int customerId = 4; // ID khách cần test
//
//        List<Workout> workouts = dao.getWorkoutsByCustomerId(customerId);
//        for (Workout w : workouts) {
//            System.out.println("[" + w.getProgramName() + "] " + w.getTitle());
//        }
//        System.out.println("Total workouts = " + workouts.size());
//
//    }
//}
