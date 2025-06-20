package main;

import dao.CustomerDAO;
import dao.TrainerDAO;
import dao.UserDAO;
import java.util.ArrayList;
import model.Customer;
import model.Trainer;
import model.User;

public class DAOTest {

    public static void main(String[] args) {
//        int testUserId = 1; // Thay đổi ID này theo user bạn muốn test
//        testUserDAO();
//        testCustomerDAO(testUserId);
//        testTrainerDAO(testUserId);
            getuser();
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

    private static void testUserDAO() {
        UserDAO userDAO = new UserDAO();
        ArrayList<User> userList = userDAO.getAllUsers();
        System.out.println(userList);
    }
    private static void getuser() {
        UserDAO userDAO = new UserDAO();
        User user = userDAO.getUserByEmail("pchanhdn@gmail.com");
        System.out.println(user);
    }
}
