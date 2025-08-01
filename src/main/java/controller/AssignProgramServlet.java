package controller;

import dao.CustomerProgramDAO;
import dao.UserDAO;
import dao.ProgramDAO;
import dao.PackageDAO;
import model.User;
import model.Program;
import model.Package;
import Utils.NotificationUtil;
import jakarta.servlet.http.HttpSession;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/AssignProgramServlet")
public class AssignProgramServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

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

        // Load programs and packages
        ProgramDAO programDAO = new ProgramDAO();
        List<Program> programs = programDAO.getAllPrograms(Integer.parseInt(userId));
        List<Package> packageList = new PackageDAO().getAllPackagesByTrainer(Integer.parseInt(userId));

        System.out.println("Programs loaded: " + (programs != null ? programs.size() : "null"));
        if (programs != null) {
            for (Program p : programs) {
                System.out.println("Program: " + p.getName() + " (ID: " + p.getProgramId() + ")");
            }
        }

        // Ensure programs is never null
        if (programs == null) {
            programs = new ArrayList<>();
        }

        request.setAttribute("programs", programs);
        request.setAttribute("packageList", packageList);

        // Load customers
        dao.ContractDAO contractDAO = new dao.ContractDAO();
        ArrayList<User> customers = contractDAO.getCustomersByTrainer(Integer.parseInt(userId));
        System.out.println("Customers loaded: " + (customers != null ? customers.size() : "null"));
        if (customers != null && !customers.isEmpty()) {
            for (User c : customers) {
                System.out.println("Customer: " + c.getUserName() + " (ID: " + c.getUserId() + ", Email: " + c.getEmail() + ")");
            }
        } else {
            System.out.println("No customers found for trainer ID: " + Integer.parseInt(userId));
            request.setAttribute("error", "No active customers found. Please ensure customers have active contracts.");
        }

        request.setAttribute("customers", customers);

        // Debug contracts
        contractDAO.debugAllContracts();

        // Set programId if provided
        String programIdStr = request.getParameter("programId");
        if (programIdStr != null) {
            request.setAttribute("programId", programIdStr);
            System.out.println("Program ID from parameter: " + programIdStr);
        }

        // Forward to programs.jsp
        System.out.println("Forwarding to programs.jsp");
        request.getRequestDispatcher("trainer/programs.jsp").forward(request, response);
        System.out.println("=== END DEBUG AssignProgramServlet.doGet ===");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int programId = Integer.parseInt(request.getParameter("programId"));
            int customerId = Integer.parseInt(request.getParameter("customerId"));
            String startDateStr = request.getParameter("startDate");

            LocalDate startDate = LocalDate.parse(startDateStr);
            LocalDate currentDate = LocalDate.now();

            // Kiểm tra startDate không được trong quá khứ
            if (startDate.isBefore(currentDate)) {
                request.setAttribute("error", "Start date cannot be in the past!");
                response.sendRedirect(request.getContextPath() + "/ProgramServlet?error=invalid_date");
                return;
            }

            CustomerProgramDAO cpDAO = new CustomerProgramDAO();

            // Kiểm tra xem chương trình đã được gán cho khách hàng này chưa
            if (cpDAO.isProgramAlreadyAssigned(customerId, programId)) {
                request.setAttribute("error", "This program is already assigned to this customer!");
                response.sendRedirect(request.getContextPath() + "/ProgramServlet?error=already_assigned");
                return;
            }

            // Gán chương trình cho khách hàng
            int result = cpDAO.assignProgramToCustomer(programId, customerId, startDate);

            if (result > 0) {
                // Gửi notification thành công
                NotificationUtil.sendSuccessNotification(customerId,
                        "Program Assigned",
                        "A new training program has been assigned to you!");

                // Gửi notification cho trainer về việc gán thành công
                HttpSession session = request.getSession();
                User trainer = (User) session.getAttribute("user");
                if (trainer != null) {
                    NotificationUtil.sendSuccessNotification(trainer.getUserId(),
                            "Program Assignment Successful",
                            "Program has been successfully assigned to the customer!");
                }

                response.sendRedirect(request.getContextPath() + "/ProgramServlet?success=assigned");
            } else {
                request.setAttribute("error", "Failed to assign program!");
                response.sendRedirect(request.getContextPath() + "/ProgramServlet?error=assignment_failed");
            }

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid input data!");
            response.sendRedirect(request.getContextPath() + "/ProgramServlet?error=invalid_input");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "An error occurred: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/ProgramServlet?error=system_error");
        }
    }
} 