package controller;

import dao.*;
import model.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Package;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@WebServlet("/purchase")
public class PurchasePageServlet extends HttpServlet {
    private ContractDAO contractDAO = new ContractDAO();
    private CustomerProgramDAO customerProgramDAO = new CustomerProgramDAO();
    private TransactionDAO transactionDAO =new TransactionDAO();
    private CustomerWorkoutScheduleDAO customerWorkoutScheduleDAO = new CustomerWorkoutScheduleDAO();
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
        if (userId == null || role == null || !role.equalsIgnoreCase("Customer")) {
            response.sendRedirect("login.jsp");
            return;
        }
        try {
            String rawPackageId = request.getParameter("packageId");
            String rawProgramId = request.getParameter("programId");
            String rawTrainerId = request.getParameter("trainerId");
            String rawPrice = request.getParameter("price");
            if (rawPackageId == null || rawProgramId == null || rawTrainerId == null) {
                response.getWriter().println("Error: Missing parameter.");
                return;
            }

            int packageId = Integer.parseInt(rawPackageId);
            PackageDAO dao = new PackageDAO();
            Package p = dao.getPackageById(packageId);

            if (p != null) {
//                request.setAttribute("pkg", p);
//                request.getRequestDispatcher("purchase.jsp").forward(request, response);
                Contract contract = new Contract();
                contract.setCustomerID(Integer.parseInt(userId));
                contract.setStatus("active");
                contract.setTrainerID(Integer.parseInt(rawTrainerId));
                LocalDate startDate = LocalDate.now();
                LocalDate endDate = startDate.plusDays(p.getDuration());
                contract.setStartDate(java.sql.Date.valueOf(startDate));
                contract.setEndDate(java.sql.Date.valueOf(endDate));
                contract.setPackageID(Integer.parseInt(rawPackageId));
                int contractId = contractDAO.createContract(contract.getTrainerID(), contract.getCustomerID(), contract.getPackageID(), contract.getStartDate(), contract.getEndDate());
                Transaction transaction = new Transaction();
                transaction.setContractId(contractId);
                transaction.setAmount(BigDecimal.valueOf(Double.parseDouble(rawPrice)));
                transaction.setStatus("completed");
                transaction.setDescription("good");
                transaction.setCreatedTime(Timestamp.valueOf(LocalDateTime.now()));
                transaction.setCustomerId(Integer.parseInt(userId));
                transactionDAO.addTransaction(transaction);
                CustomerProgram customerProgram = new CustomerProgram();
                customerProgram.setCustomerId(Integer.parseInt(userId));
                customerProgram.setProgramId(Integer.parseInt((rawProgramId)));
                customerProgram.setAssignedAt(LocalDateTime.now());
                LocalDate curent = LocalDate.now();
                customerProgram.setStartDate(Date.valueOf(curent));
                customerProgram.setEndDate(Date.valueOf(curent.plusDays(p.getDuration())));
                int customerProgramId = customerProgramDAO.assignProgramToCustomer(customerProgram.getProgramId(), customerProgram.getCustomerId(), customerProgram.getStartDate(), customerProgram.getEndDate());
                CustomerWorkoutSchedule schedule = new CustomerWorkoutSchedule();
                schedule.setCustomerProgramId(customerProgramId);
                schedule.setStartAt(contract.getStartDate());
                schedule.setEndAt(contract.getEndDate());
                schedule.setStatus("pending");
                customerWorkoutScheduleDAO.addSchedule(schedule);
                request.getRequestDispatcher("packagesPurchased").forward(request, response);
            } else {
                response.sendRedirect("not-found.jsp");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Error: " + e.getMessage());
        }
    }
}
