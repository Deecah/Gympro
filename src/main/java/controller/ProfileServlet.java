
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.CustomerDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Customer;
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

        String name = request.getParameter("name");
        String gender = request.getParameter("gender");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");

        // Update user basic info
        user.setUserName(name);
        user.setGender(gender);
        user.setPhone(phone);
        user.setAddress(address);

        // Get customer specific data
        Customer customer = (Customer) session.getAttribute("customer");
        double weight = Double.parseDouble(request.getParameter("weight"));
        double height = Double.parseDouble(request.getParameter("height"));
        String goal = request.getParameter("goal");
        String medical = request.getParameter("medicalConditions");

        customer.setWeight(weight);
        customer.setHeight(height);
        customer.setGoal(goal);
        customer.setMedicalConditions(medical);

        // Update customer profile
        CustomerDAO dao = new CustomerDAO();
        boolean success = dao.editProfile(customer);

        if (success) {
            // Update session data
            session.setAttribute("customer", customer);
            session.setAttribute("user", user);
            response.sendRedirect("profile.jsp?success=updated");
        } else {
            request.setAttribute("error", "Failed to update profile.");
            request.getRequestDispatcher("editprofile.jsp").forward(request, response);
        }
    }
}
