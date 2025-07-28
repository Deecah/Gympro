package controller;

import dao.ContractDAO;
import dao.ProgressDAO;
import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.*;

import model.User;
import model.CustomerProgressDTO;
import model.WorkoutPostDTO;

@WebServlet(name = "CustomerServlet", urlPatterns = {"/CustomerServlet"})
public class CustomerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        User user = (User) session.getAttribute("user");
        if (!"Trainer".equalsIgnoreCase(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String action = request.getParameter("action");
        
        try {
            if ("viewProgress".equals(action)) {
                // Hiển thị chi tiết progress của một customer cụ thể
                String customerIdParam = request.getParameter("customerId");
                String programIdParam = request.getParameter("programId");
                
                if (customerIdParam != null && programIdParam != null) {
                    int customerId = Integer.parseInt(customerIdParam);
                    int programId = Integer.parseInt(programIdParam);
                    showCustomerProgressDetail(request, response, customerId, programId);
                    return;
                }
            }
            
            // Hiển thị danh sách customers với progress overview
            showCustomersList(request, response, user.getUserId());
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(500, "Internal server error");
        }
    }

    private void showCustomersList(HttpServletRequest request, HttpServletResponse response, int trainerId) 
            throws ServletException, IOException {
        
        // Lấy danh sách khách hàng có contract với trainer
        ContractDAO contractDAO = new ContractDAO();
        java.util.ArrayList<User> customers = contractDAO.getCustomersByTrainer(trainerId);
        
        // Lấy thông tin progress cho từng khách hàng
        ProgressDAO progressDAO = new ProgressDAO();
        java.util.ArrayList<CustomerProgressDTO> customerProgressList = new java.util.ArrayList<>();
        
        for (User customer : customers) {
            // Lấy thống kê progress theo từng program cho customer này
            List<CustomerProgressDTO> programStats = progressDAO.getProgramProgressStats(customer.getUserId());
            
            for (CustomerProgressDTO programProgress : programStats) {
                // Set thông tin customer cho mỗi program progress
                programProgress.setCustomer(customer);
                customerProgressList.add(programProgress);
            }
        }
        
        request.setAttribute("customers", customerProgressList);
        request.getRequestDispatcher("trainer/customer.jsp").forward(request, response);
    }
    
    private void showCustomerProgressDetail(HttpServletRequest request, HttpServletResponse response, 
                                          int customerId, int programId) throws ServletException, IOException {
        
        ProgressDAO progressDAO = new ProgressDAO();
        UserDAO userDAO = new UserDAO();
        
        // Lấy thông tin customer
        User customer = userDAO.getUserById(customerId);
        if (customer == null) {
            // Redirect về trang customers với thông báo lỗi
            request.setAttribute("error", "Customer not found");
            showCustomersList(request, response, ((User)request.getSession().getAttribute("user")).getUserId());
            return;
        }
        
        // Lấy danh sách workouts với trạng thái hoàn thành cho program này
        List<WorkoutPostDTO> workouts = progressDAO.getWorkoutsByProgramWithStatus(customerId, programId);
        
        // Lấy thống kê progress cho program này
        List<CustomerProgressDTO> programStats = progressDAO.getProgramProgressStats(customerId);
        CustomerProgressDTO programProgress = null;
        for (CustomerProgressDTO stat : programStats) {
            if (stat.getProgramId() == programId) {
                programProgress = stat;
                programProgress.setCustomer(customer);
                break;
            }
        }
        
        // Nếu không tìm thấy program progress, tạo một object mặc định
        if (programProgress == null) {
            programProgress = new CustomerProgressDTO();
            programProgress.setCustomer(customer);
            programProgress.setProgramId(programId);
            programProgress.setProgramName("Unknown Program");
            programProgress.setTotalWorkouts(0);
            programProgress.setCompletedWorkouts(0);
            programProgress.setProgressPercent(0.0);
        }
        
        request.setAttribute("customer", customer);
        request.setAttribute("programProgress", programProgress);
        request.setAttribute("workouts", workouts);
        request.getRequestDispatcher("trainer/customerProgressDetail.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
} 