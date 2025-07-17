
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.CustomerDAO;
import dao.TrainerDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Customer;
import model.Trainer;
import model.User;

@WebServlet(name = "ProfileServlet", urlPatterns = {"/ProfileServlet"})
public class ProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("editprofile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String role = user.getRole();

        String name = request.getParameter("name");
        String gender = request.getParameter("gender");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");

        boolean success = false;

        if ("Customer".equalsIgnoreCase(role)) {
            Customer customer = (Customer) session.getAttribute("customer");
            user.setUserName(name);
            user.setGender(gender);
            user.setPhone(phone);
            user.setAddress(address);
            
            double weight = Double.parseDouble(request.getParameter("weight"));
            double height = Double.parseDouble(request.getParameter("height"));
            String goal = request.getParameter("goal");
            String medical = request.getParameter("medicalConditions");

            customer.setWeight(weight);
            customer.setHeight(height);
            customer.setGoal(goal);
            customer.setMedicalConditions(medical);

            CustomerDAO dao = new CustomerDAO();
            success = dao.editProfile(customer);

            if (success) {
                session.setAttribute("customer", customer);
                user.setUserName(name);
                user.setGender(gender);
                user.setPhone(phone);
                user.setAddress(address);
                session.setAttribute("user", user);
            }

        } else if ("Trainer".equalsIgnoreCase(role)) {
            Trainer trainer = (Trainer) session.getAttribute("trainer");
            user.setUserName(name);
            user.setGender(gender);
            user.setPhone(phone);
            user.setAddress(address);

            int experience = Integer.parseInt(request.getParameter("experienceYears"));
            String description = request.getParameter("description");
            String specialization = request.getParameter("specialization");

            trainer.setExperienceYears(experience);
            trainer.setDescription(description);
            trainer.setSpecialization(specialization);

            TrainerDAO dao = new TrainerDAO();
            success = dao.editProfile(trainer);

            if (success) {
                session.setAttribute("trainer", trainer);
                user.setUserName(name);
                user.setGender(gender);
                user.setPhone(phone);
                user.setAddress(address);
                session.setAttribute("user", user);
            }
        }

        if (success) {
            if("Trainer".equalsIgnoreCase(role)) {
                response.sendRedirect("profile-trainer.jsp");
            } else if("Customer".equalsIgnoreCase(role)){
                response.sendRedirect("profile.jsp");
            }
        } else {
            request.setAttribute("error", "Failed to update profile.");
            request.getRequestDispatcher("editprofile.jsp").forward(request, response);
        }
    }

}
