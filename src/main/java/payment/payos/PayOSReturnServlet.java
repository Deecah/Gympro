package payment.payos;

import dao.ChatDAO;
import dao.ContractDAO;
import dao.CustomerProgramDAO;
import dao.ProgramDAO;
import dao.TransactionDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import model.Transaction;
import Utils.NotificationUtil;

@WebServlet("/payos/return")
public class PayOSReturnServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String orderCode = request.getParameter("orderCode");  // = transactionId
        String status = request.getParameter("status");        // success | cancel

        boolean transSuccess = false;

        try {
            int transactionId = Integer.parseInt(orderCode);
            TransactionDAO transactionDAO = new TransactionDAO();
            model.Transaction transaction = transactionDAO.getTransactionById(transactionId);
            if (transaction == null) throw new Exception("Không tìm thấy transaction với ID: " + transactionId);

            if ("success".equals(status)) {
                transactionDAO.updateTransactionStatus(transactionId, "Completed");
                System.out.println("Transaction marked as Completed: " + transactionId);

                // Lấy thông tin từ session (giống PayPal/VNPay)
                HttpSession session = request.getSession();
                Integer customerId = (Integer) session.getAttribute("loggedInCustomerId");
                Integer trainerId = (Integer) session.getAttribute("selectedTrainerId");
                Integer packageId = (Integer) session.getAttribute("selectedPackageId");
                Integer duration = (Integer) session.getAttribute("selectedPackageDuration");
                System.out.println("[PayOSReturn] customerId=" + customerId + ", trainerId=" + trainerId + ", packageId=" + packageId + ", duration=" + duration);

                if (customerId == null || trainerId == null || packageId == null || duration == null) {
                    throw new Exception("Session thiếu thông tin để xử lý thanh toán PayOS.");
                }

                LocalDateTime start = LocalDateTime.now();
                LocalDateTime end = start.plusDays(duration);

                ContractDAO contractDAO = new ContractDAO();
                contractDAO.createContract(trainerId, customerId, packageId, start, end);
                System.out.println("Contract created");

                ProgramDAO programDAO = new ProgramDAO();
                CustomerProgramDAO cpDAO = new CustomerProgramDAO();
                int programId = programDAO.getProgramIdByPackageId(packageId);
                System.out.println("Program ID resolved: " + programId);

                if (programId > 0 && !cpDAO.isProgramAlreadyAssigned(customerId, programId)) {
                    cpDAO.assignProgramToCustomer(programId, customerId, LocalDate.from(start));
                    System.out.println("Program assigned to customer");
                }

                ChatDAO chatDAO = new ChatDAO();
                boolean canChat = chatDAO.isChatAllowed(customerId, trainerId);
                System.out.println("isChatAllowed: " + canChat);

                if (canChat) {
                    int chatId = chatDAO.createChatIfNotExists(customerId, trainerId);
                    System.out.println("Chat created with ID: " + chatId);
                }
                
                // Gửi notification thành công cho customer
                NotificationUtil.sendSuccessNotification(customerId, 
                    "Payment Successful!", 
                    "Your package purchase has been completed successfully via PayOS!");
                
                // Gửi notification cho trainer về khách hàng mới
                NotificationUtil.sendInfoNotification(trainerId, 
                    "New Customer Purchase", 
                    "A new customer has purchased your package!");

                // Dọn session
                session.removeAttribute("selectedTrainerId");
                session.removeAttribute("selectedPackageId");
                session.removeAttribute("selectedPackageDuration");

                transSuccess = true;
            } else {
                transactionDAO.updateTransactionStatus(transactionId, "Fail");
                System.out.println("Thanh toán thất bại hoặc bị hủy.");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute("error", "Lỗi xử lý PayOSReturn: " + ex.getMessage());
        }

        request.setAttribute("transResult", transSuccess);
        request.getRequestDispatcher("/paymentResult.jsp").forward(request, response);
    }
}
