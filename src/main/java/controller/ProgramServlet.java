package controller;

import dao.PackageDAO;
import dao.ProgramDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

import model.Package;
import model.Program;
import model.User;
import Utils.NotificationUtil;

@WebServlet(name = "ProgramServlet", urlPatterns = {"/ProgramServlet"})
public class ProgramServlet extends HttpServlet {

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

        ProgramDAO dao = new ProgramDAO();
        List<Program> programs = dao.getAllProgramsByTrainer(user.getUserId());
        List<Package> packageList = new PackageDAO().getAllPackagesByTrainer(user.getUserId());

        request.setAttribute("programs", programs);
        request.setAttribute("packageList", packageList);

        // Load danh sách khách hàng có contract với trainer cho modal gán chương trình
        dao.ContractDAO contractDAO = new dao.ContractDAO();
        java.util.ArrayList<User> customers = contractDAO.getCustomersByTrainer(user.getUserId());
        request.setAttribute("customers", customers);

        // Xử lý các thông báo từ AssignProgramServlet
        String error = request.getParameter("error");
        String success = request.getParameter("success");
        
        if (error != null) {
            switch (error) {
                case "invalid_date":
                    request.setAttribute("error", "Start date cannot be in the past!");
                    break;
                case "already_assigned":
                    request.setAttribute("error", "This program is already assigned to this customer!");
                    break;
                case "assignment_failed":
                    request.setAttribute("error", "Failed to assign program!");
                    break;
                case "invalid_input":
                    request.setAttribute("error", "Invalid input data!");
                    break;
                case "system_error":
                    request.setAttribute("error", "An error occurred while processing your request!");
                    break;
                default:
                    request.setAttribute("error", "An error occurred!");
                    break;
            }
        }
        
        if (success != null) {
            switch (success) {
                case "assigned":
                    request.setAttribute("success", "Program has been successfully assigned to the customer!");
                    break;
                default:
                    request.setAttribute("success", "Operation completed successfully!");
                    break;
            }
        }

        request.getRequestDispatcher("trainer/programs.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
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

        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String packageIdStr = request.getParameter("packageId");

        Program program = new Program();
        program.setName(name);
        program.setDescription(description);

        try {
            int packageId = Integer.parseInt(packageIdStr);
            program.setPackageId(packageId);
        } catch (NumberFormatException e) {
            program.setPackageId(0); 
        }

        ProgramDAO dao = new ProgramDAO();
        int result = dao.addProgram(program, user.getUserId());
        boolean success = result > 0;

        if (success) {
            // Gửi notification thành công
            NotificationUtil.sendSuccessNotification(user.getUserId(), 
                "Program Created Successfully", 
                "Your new program '" + name + "' has been created successfully!");
        } else {
            // Gửi notification lỗi
            NotificationUtil.sendErrorNotification(user.getUserId(), 
                "Program Creation Failed", 
                "Failed to create program '" + name + "'. Please try again.");
        }

        response.sendRedirect(request.getContextPath() + "/ProgramServlet");
    }
}
