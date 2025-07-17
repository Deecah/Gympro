package controller;

import dao.ScheduleDAO;
import model.Slot;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/ScheduleServlet")
public class ScheduleServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer customerId = (Integer) request.getSession().getAttribute("customerId");
        if (customerId == null) {
            response.sendRedirect("login.jsp");
            return;
        }// get from session ideally
        List<Slot> slots = ScheduleDAO.getSlotsByCustomerId(customerId);
        request.setAttribute("slots", slots);
        RequestDispatcher rd = request.getRequestDispatcher("Schedule.jsp");
        rd.forward(request, response);
    }
}
