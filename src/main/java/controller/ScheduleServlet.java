package controller;

import dao.CustomerProgramDAO;
import jakarta.servlet.http.*;
import model.CustomerProgram;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import model.CustomerProgramDTO;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@WebServlet("/ScheduleServlet")
public class ScheduleServlet extends HttpServlet {

    private CustomerProgramDAO customerProgramDAO = new CustomerProgramDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String userId = null;
        String role = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equalsIgnoreCase("userId") && cookie.getValue() != null) {
                userId = cookie.getValue();
            } else if (cookie.getName().equalsIgnoreCase("role") && cookie.getValue() != null) {
                role = cookie.getValue();
            }
        }
        if (userId == null || role == null || !role.equalsIgnoreCase("Trainer")) {
            response.sendRedirect("login.jsp");
            return;
        }
        int trainerId = Integer.parseInt(userId);

        // Lấy danh sách CustomerProgram theo trainerId
        List<CustomerProgramDTO> customerPrograms = customerProgramDAO.getCustomerProgramsByTrainerId(trainerId);

        // Truyền dữ liệu vào JSP
        request.setAttribute("customerPrograms", customerPrograms);
        request.setAttribute("trainerId", trainerId);
        request.getRequestDispatcher("trainer/schedule.jsp").forward(request, response);
    }
}