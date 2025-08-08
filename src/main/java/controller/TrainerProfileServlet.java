package controller;

import dao.TrainerDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Trainer;
import model.User;

@WebServlet(name = "TrainerProfileServlet", urlPatterns = {"/TrainerProfileServlet"})
public class TrainerProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        // Check if user is a trainer
        if (!"Trainer".equalsIgnoreCase(user.getRole())) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        // Forward to trainer profile edit page
        request.getRequestDispatcher("trainer/edit-profile-trainer.jsp").forward(request, response);
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
        
        // Check if user is a trainer
        if (!"Trainer".equalsIgnoreCase(user.getRole())) {
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

        // Get trainer specific data
        Trainer trainer = (Trainer) session.getAttribute("trainer");
        int experience = Integer.parseInt(request.getParameter("experience"));
        String description = request.getParameter("description");
        String specialization = request.getParameter("specialization");

        trainer.setExperienceYears(experience);
        trainer.setDescription(description);
        trainer.setSpecialization(specialization);

        // Update trainer profile
        TrainerDAO dao = new TrainerDAO();
        boolean success = dao.editProfile(trainer);

        if (success) {
            // Update session data
            session.setAttribute("trainer", trainer);
            session.setAttribute("user", user);
            response.sendRedirect("trainer/profile-trainer.jsp?success=updated");
        } else {
            request.setAttribute("error", "Failed to update trainer profile.");
            request.getRequestDispatcher("trainer/edit-profile-trainer.jsp").forward(request, response);
        }
    }
} 