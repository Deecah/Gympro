package payment.vnpay;

import dao.ContractDAO;
import dao.PackageDAO;
import dao.TransactionDAO;
import dao.ChatDAO;
import dao.CustomerProgramDAO;
import dao.ProgramDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import model.Transaction;
import Utils.NotificationUtil;

@WebServlet(name = "VnpayReturn", urlPatterns = {"/vnpay/vnpayReturn"})
public class VnpayReturn extends HttpServlet {

    TransactionDAO transactionDAO = new TransactionDAO();
    PackageDAO packageDAO = new PackageDAO();
    ContractDAO contractDAO = new ContractDAO();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Map<String, String> fields = new HashMap<>();
        for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements();) {
            String fieldName = params.nextElement();
            String fieldValue = request.getParameter(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                fields.put(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII),
                        URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
            }
        }

        String vnp_SecureHash = request.getParameter("vnp_SecureHash");
        fields.remove("vnp_SecureHashType");
        fields.remove("vnp_SecureHash");

        String signValue = Config.hashAllFields(fields);
        boolean transSuccess = false;

        if (signValue.equals(vnp_SecureHash)) {
            try {
                String vnpTxnRef = request.getParameter("vnp_TxnRef");
                int transactionId = Integer.parseInt(vnpTxnRef.split("_")[0]);
                String transactionStatus = request.getParameter("vnp_TransactionStatus");
                int packageId = Integer.parseInt(request.getParameter("vnp_OrderInfo"));

                System.out.println("✅ VNPay TransactionID: " + transactionId);
                System.out.println("✅ Transaction Status: " + transactionStatus);

                if ("00".equals(transactionStatus)) {
                    transactionDAO.updateTransactionStatus(transactionId, "Completed");
                    System.out.println("✅ Transaction marked as Completed");

                    Transaction transaction = transactionDAO.getTransactionById(transactionId);
                    int customerId = transaction.getCustomerId();

                    int trainerId = packageDAO.getTrainerIdByPackage(packageId);
                    int durationDays = packageDAO.getDurationByPackage(packageId);

                    LocalDateTime startDate = LocalDateTime.now();
                    LocalDateTime endDate = startDate.plusDays(durationDays);

                    contractDAO.createContract(trainerId, customerId, packageId, startDate, endDate);
                    System.out.println("✅ Contract created");

                    ProgramDAO programDAO = new ProgramDAO();
                    CustomerProgramDAO cpDAO = new CustomerProgramDAO();
                    int programId = programDAO.getProgramIdByPackageId(packageId);
                    System.out.println("✅ Program ID resolved: " + programId);

                    if (programId > 0 && !cpDAO.isProgramAlreadyAssigned(customerId, programId)) {
                        cpDAO.assignProgramToCustomer(programId, customerId, LocalDate.from(startDate));
                        System.out.println("✅ Program assigned to customer");
                    }

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
                        "Your package purchase has been completed successfully via VNPay!");
                    
                    // Gửi notification cho trainer về khách hàng mới
                    NotificationUtil.sendInfoNotification(trainerId, 
                        "New Customer Purchase", 
                        "A new customer has purchased your package!");

                    transSuccess = true;
                } else {
                    transactionDAO.updateTransactionStatus(transactionId, "Fail");
                    System.out.println("❌ Transaction failed with status: " + transactionStatus);
                }

            } catch (Exception e) {
                e.printStackTrace(); // Hiện lỗi ra console
            }

            request.setAttribute("transResult", transSuccess);
            request.getRequestDispatcher("paymentResult.jsp").forward(request, response);

        } else {
            System.out.println("❌ Giao dịch không hợp lệ (Invalid signature)");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
