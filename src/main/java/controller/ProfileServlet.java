/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import dao.CustomerDAO;
import dao.TrainerDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Customer;
import model.Trainer;
import model.User;

public class ProfileServlet extends HttpServlet {
    private final CustomerDAO customerDAO = new CustomerDAO();
    private final TrainerDAO trainerDAO = new TrainerDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        User user = (User) session.getAttribute("user");
        String role = user.getRole();

        if ("Customer".equalsIgnoreCase(role)) {
            Customer customer = customerDAO.getProfile(user.getUserId());
            session.setAttribute("customer", customer);
        } else if ("Trainer".equalsIgnoreCase(role)) {
            Trainer trainer = trainerDAO.getProfile(user.getUserId());
            session.setAttribute("trainer", trainer);
        }

        // Chuyển tới JSP
        request.getRequestDispatcher("profile.jsp").forward(request, response);
    }
}
