package main;

import dao.CustomerDAO;
import dao.TrainerDAO;
import model.Customer;
import model.Trainer;

public class DAOTest {
    public static void main(String[] args) {
        int testUserId = 1; // Thay đổi ID này theo user bạn muốn test

        testCustomerDAO(testUserId);
        testTrainerDAO(testUserId);
    }

    private static void testCustomerDAO(int userId) {
        CustomerDAO customerDAO = new CustomerDAO();
        Customer customer = customerDAO.getProfile(userId);
        if (customer == null) {
            System.out.println("❌ CustomerDAO.getProfile() trả về null cho userId = " + userId);
        } else {
            System.out.println("✅ Customer loaded:");
            System.out.println("Name: " + customer.getUserName());
            System.out.println("Weight: " + customer.getWeight());
            System.out.println("Height: " + customer.getHeight());
        }
    }

    private static void testTrainerDAO(int userId) {
        TrainerDAO trainerDAO = new TrainerDAO();
        Trainer trainer = trainerDAO.getProfile(userId);
        if (trainer == null) {
            System.out.println("❌ TrainerDAO.getProfile() trả về null cho userId = " + userId);
        } else {
            System.out.println("✅ Trainer loaded:");
            System.out.println("Name: " + trainer.getUserName());
            System.out.println("Experience: " + trainer.getExperienceYears());
            System.out.println("Specialization: " + trainer.getSpecialization());
        }
    }
}
