package payment.paypal;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;

import dao.ContractDAO;
import dao.TransactionDAO;
import model.Transaction;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
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
            transaction.setType("PayPal");
            transaction.setDescription("Thanh toán qua PayPal - Gói tập GymPro");

            TransactionDAO transactionDAO = new TransactionDAO();
            int transactionId = transactionDAO.addTransaction(transaction);

            // 5. Nếu giao dịch thành công → tạo hợp đồng
            if (transactionId > 0) {
                LocalDate start = LocalDate.now();
                LocalDate end = start.plusDays(duration);

                ContractDAO contractDAO = new ContractDAO();
                contractDAO.createContract(trainerId, customerId, packageId, start, end);
            }

            // 6. Dọn session
            session.removeAttribute("selectedTrainerId");
            session.removeAttribute("selectedPackageId");
            session.removeAttribute("selectedPackageDuration");

            request.setAttribute("transResult", true);
            request.getRequestDispatcher("/paymentResult.jsp").forward(request, response);

        } catch (Exception ex) {
            Logger.getLogger(PayPalExecutePaymentServlet.class.getName()).log(Level.SEVERE, null, ex);
            request.setAttribute("transResult", false);
            request.getRequestDispatcher("/paymentResult.jsp").forward(request, response);
        }
    }
}
