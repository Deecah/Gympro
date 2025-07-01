package vnpay;

import dao.ContractDAO;
import dao.PackageDAO;
import dao.TransactionDAO;
import dao.ChatDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import model.Transaction;

@WebServlet(name = "VnpayReturn", urlPatterns = {"/vnpayReturn"})
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
        if (signValue.equals(vnp_SecureHash)) {
            String vnpTxnRef = request.getParameter("vnp_TxnRef");
            int transactionId = Integer.parseInt(vnpTxnRef.split("_")[0]);
            String transactionStatus = request.getParameter("vnp_TransactionStatus");
            int packageId = Integer.parseInt(request.getParameter("vnp_OrderInfo"));

            boolean transSuccess = false;

            if ("00".equals(transactionStatus)) {
                transactionDAO.updateTransactionStatus(transactionId, "Completed");

                Transaction transaction = transactionDAO.getTransactionById(transactionId);
                int customerId = transaction.getCustomerId();

                int trainerId = packageDAO.getTrainerIdByPackage(packageId);
                int durationDays = packageDAO.getDurationByPackage(packageId);

                LocalDate startDate = LocalDate.now();
                LocalDate endDate = startDate.plusDays(durationDays);

                contractDAO.createContract(trainerId, customerId, packageId, startDate, endDate);

                // tạo Chat
                ChatDAO chatDAO = new ChatDAO();
                if (chatDAO.isChatAllowed(customerId, trainerId)) {
                    chatDAO.createChatIfNotExists(customerId, trainerId);
                }

                transSuccess = true;
            } else {
                transactionDAO.updateTransactionStatus(transactionId, "Fail");
            }

            request.setAttribute("transResult", transSuccess);
            request.getRequestDispatcher("paymentResult.jsp").forward(request, response);
        } else {
            System.out.println("Giao dịch không hợp lệ (Invalid signature)");
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