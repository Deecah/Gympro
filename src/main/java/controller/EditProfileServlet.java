package controller;


import dao.UserDAO;
import dao.CustomerDAO;
import dao.TrainerDAO;
import model.User;
import model.Customer;
import model.Trainer;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import Utils.NotificationUtil;

@WebServlet(name = "EditProfileServlet", urlPatterns = {"/EditProfileServlet"})
public class EditProfileServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        int id = Integer.parseInt(request.getParameter("id"));
        String userName = request.getParameter("username"); // <--- trùng với input name trong JSP
        String gender = request.getParameter("gender");
        String address = request.getParameter("address");
        String role = request.getParameter("role");

        // Update User info
        User user = new User();
        user.setUserId(id);
        user.setUserName(userName);
        user.setGender(gender);
        user.setAddress(address);
        user.setRole(role);

        UserDAO userDAO = new UserDAO();
        boolean success = userDAO.updateUser(user);

        // Update role-specific data
        if ("Customer".equalsIgnoreCase(role)) {
            try {
                Double weight = Double.parseDouble(request.getParameter("weight"));
                Double height = Double.parseDouble(request.getParameter("height"));
                String goal = request.getParameter("goal");
                String medicalConditions = request.getParameter("medicalConditions");

                Customer customer = new Customer();
                customer.setUserId(id);
                customer.setWeight(weight);
                customer.setHeight(height);
                customer.setGoal(goal);
                customer.setMedicalConditions(medicalConditions);

                CustomerDAO customerDAO = new CustomerDAO();
                customerDAO.editProfile(customer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("Trainer".equalsIgnoreCase(role)) {
            try {
                int experienceYears = Integer.parseInt(request.getParameter("experienceYears"));
                String specialization = request.getParameter("specialization");
                String description = request.getParameter("description");

                Trainer trainer = new Trainer();
                trainer.setUserId(id);
                trainer.setExperienceYears(experienceYears);
                trainer.setSpecialization(specialization);
                trainer.setDescription(description);

                TrainerDAO trainerDAO = new TrainerDAO();
                trainerDAO.editProfile(trainer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Update session
        if (success) {
            User updatedUser = userDAO.getUserById(id);
            HttpSession session = request.getSession();
            session.setAttribute("user", updatedUser);

            if ("Customer".equalsIgnoreCase(role)) {
                Customer updatedCustomer = new CustomerDAO().getProfile(id);
                session.setAttribute("customer", updatedCustomer);
            } else if ("Trainer".equalsIgnoreCase(role)) {
                Trainer updatedTrainer = new TrainerDAO().getProfile(id);
                session.setAttribute("trainer", updatedTrainer);
            }
            
            // Gửi notification thành công
            NotificationUtil.sendSuccessNotification(id, 
                "Profile Updated Successfully", 
                "Your profile information has been updated successfully!");
        } else {
            // Gửi notification lỗi
            NotificationUtil.sendErrorNotification(id, 
                "Profile Update Failed", 
                "Failed to update your profile. Please try again.");
        }

        response.sendRedirect("profile.jsp");
    }
}
