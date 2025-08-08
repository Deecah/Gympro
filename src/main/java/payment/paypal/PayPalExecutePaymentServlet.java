package payment.paypal;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import dao.ChatDAO;

import dao.ContractDAO;
import dao.CustomerProgramDAO;
import dao.ProgramDAO;
import dao.TransactionDAO;
import model.Transaction;
import Utils.NotificationUtil;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/paypal/execute-payment")
public class PayPalExecutePaymentServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String paymentId = request.getParameter("paymentId");
        String payerId = request.getParameter("PayerID");

        try {
            APIContext apiContext = PayPalConfig.getAPIContext();

            // 1. Thực hiện giao dịch PayPal
            Payment payment = new Payment().setId(paymentId);
            PaymentExecution execution = new PaymentExecution().setPayerId(payerId);
            Payment executedPayment = payment.execute(apiContext, execution);

            // 2. Lấy số tiền thanh toán
            String amountStr = executedPayment.getTransactions().get(0).getAmount().getTotal();
            BigDecimal amount = new BigDecimal(amountStr);

            // 3. Lấy dữ liệu từ session
            HttpSession session = request.getSession();
            Integer customerId = (Integer) session.getAttribute("loggedInCustomerId");
            Integer trainerId = (Integer) session.getAttribute("selectedTrainerId");
            Integer packageId = (Integer) session.getAttribute("selectedPackageId");
            Integer duration = (Integer) session.getAttribute("selectedPackageDuration");

            if (customerId == null || trainerId == null || packageId == null || duration == null) {
                throw new Exception("Thiếu thông tin session để tạo hợp đồng.");
            }

            // 4. Ghi giao dịch
            Transaction transaction = new Transaction();
            transaction.setCustomerId(customerId);
            transaction.setAmount(amount);
            transaction.setStatus("Completed");
            transaction.setCreatedTime(Timestamp.valueOf(LocalDateTime.now()));
            transaction.setDescription("Thanh toán qua PayPal - Gói tập GymPro");

            TransactionDAO transactionDAO = new TransactionDAO();
            int transactionId = transactionDAO.addTransaction(transaction);
            System.out.println("✅ Transaction recorded with ID: " + transactionId);

            if (transactionId > 0) {
                LocalDate start = LocalDate.now();
                LocalDate end = start.plusDays(duration);

                ContractDAO contractDAO = new ContractDAO();
                contractDAO.createContract(trainerId, customerId, packageId, Date.valueOf(start),Date.valueOf(end));
                System.out.println("✅ Contract created");

                // 6. Gán chương trình (nếu chưa)
                ProgramDAO programDAO = new ProgramDAO();
                CustomerProgramDAO cpDAO = new CustomerProgramDAO();
                int programId = programDAO.getProgramIdByPackageId(packageId);
                System.out.println("✅ Program ID resolved: " + programId);

                if (programId > 0 && !cpDAO.isProgramAlreadyAssigned(customerId, programId)) {
                    cpDAO.assignProgramToCustomer(programId, customerId, Date.valueOf(start),Date.valueOf(end));
                    System.out.println("✅ Program assigned to customer");
                }

                // 7. Tạo Chat sau khi có Contract (nếu hợp lệ)
                ChatDAO chatDAO = new ChatDAO();
                boolean canChat = chatDAO.isChatAllowed(customerId, trainerId);
                System.out.println("✅ isChatAllowed: " + canChat);
                if (canChat) {
                    int chatId = chatDAO.createChatIfNotExists(customerId, trainerId);
                    System.out.println("✅ Chat created with ID: " + chatId);
                }
                
                // Gửi notification thành công cho customer
                NotificationUtil.sendSuccessNotification(customerId, 
                    "Payment Successful!", 
                    "Your package purchase has been completed successfully via PayPal!");
                
                // Gửi notification cho trainer về khách hàng mới
                NotificationUtil.sendInfoNotification(trainerId, 
                    "New Customer Purchase", 
                    "A new customer has purchased your package!");
            }

            // 8. Dọn session
            session.removeAttribute("selectedTrainerId");
            session.removeAttribute("selectedPackageId");
            session.removeAttribute("selectedPackageDuration");

            request.setAttribute("transResult", true);
            request.getRequestDispatcher("/paymentResult.jsp").forward(request, response);

        } catch (Exception ex) {
            ex.printStackTrace(); 
            Logger.getLogger(PayPalExecutePaymentServlet.class.getName()).log(Level.SEVERE, null, ex);
            request.setAttribute("transResult", false);
            request.getRequestDispatcher("/paymentResult.jsp").forward(request, response);
        }
    }
}
