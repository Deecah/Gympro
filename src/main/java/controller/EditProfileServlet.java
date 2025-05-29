package controller;


import model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/EditProfileServlet")
public class EditProfileServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {


        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");


        if (user != null) {
            // Update user information
            user.setName(request.getParameter("name"));
            user.setGender(request.getParameter("gender"));
            user.setEmail(request.getParameter("email"));
            user.setPhone(request.getParameter("phone"));
            user.setAddress(request.getParameter("address"));
            user.setRole(request.getParameter("role"));
            user.setStatus(request.getParameter("status"));


            // Optional: You can save the updated user to a database

            // Update the session
            session.setAttribute("user", user);


            response.sendRedirect("profile.jsp");
        } else {
            response.sendRedirect("index.jsp");
        }
    }
}