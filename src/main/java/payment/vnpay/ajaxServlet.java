package payment.vnpay;

import dao.PackageDAO;
import dao.TransactionDAO;
import model.Transaction;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@WebServlet(name = "ajaxServlet", urlPatterns = {"/vnpay/payment"})
public class ajaxServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String bankCode = req.getParameter("bankCode");
        String packageIdStr = req.getParameter("packageId");

        if (packageIdStr == null) {
            resp.sendRedirect("CustomerPackageServlet");
            return;
        }

        int packageId = Integer.parseInt(packageIdStr);

        HttpSession session = req.getSession();
        Integer customerId = (Integer) session.getAttribute("userId");
        if (customerId == null) {
            resp.sendRedirect("LoginServlet");
            return;
        }
        PackageDAO packageDAO = new PackageDAO();
        BigDecimal amount = packageDAO.getPriceByPackageId(packageId);
        double amountDouble = amount.doubleValue();

        // Create new transaction
        Transaction transaction = new Transaction();
        transaction.setCustomerId(customerId);
        transaction.setAmount(amount);
        transaction.setStatus("Processing");
        transaction.setType("VNPay"); 
        transaction.setDescription("Thanh toán qua VNPay - Gói tập GymPro");

        TransactionDAO transactionDao = new TransactionDAO();
        int transactionId = transactionDao.addTransaction(transaction);
        if (transactionId < 1) {
            System.err.println("[VNPAY] ❌ Failed to create transaction.");
            resp.sendRedirect("CustomerPackageServlet");
            return;
        }
        System.out.println("[VNPAY] ✅ Transaction created, ID = " + transactionId);

        // Build TxnRef: transactionId + timestamp to ensure uniqueness
        String vnp_TxnRef = transactionId + "_" + System.currentTimeMillis();
        String vnp_IpAddr = Config.getIpAddress(req);
        long vnp_Amount = (long) (amountDouble * 100); // Convert to smallest currency unit
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", "2.1.0");
        vnp_Params.put("vnp_Command", "pay");
        vnp_Params.put("vnp_TmnCode", Config.vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(vnp_Amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef); // Ensure unique value!
        vnp_Params.put("vnp_OrderInfo", String.valueOf(packageId));
        vnp_Params.put("vnp_OrderType", "other");
        vnp_Params.put("vnp_ReturnUrl", Config.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        vnp_Params.put("vnp_Locale", Optional.ofNullable(req.getParameter("language")).orElse("vn"));

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        vnp_Params.put("vnp_CreateDate", formatter.format(cal.getTime()));
        cal.add(Calendar.MINUTE, 15);
        vnp_Params.put("vnp_ExpireDate", formatter.format(cal.getTime()));
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        for (String fieldName : fieldNames) {
            String fieldValue = vnp_Params.get(fieldName);
            hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
            query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII))
                 .append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
            if (!fieldName.equals(fieldNames.get(fieldNames.size() - 1))) {
                hashData.append('&');
                query.append('&');
            }
        }

        String vnp_SecureHash = Config.hmacSHA512(Config.secretKey, hashData.toString());
        query.append("&vnp_SecureHash=").append(vnp_SecureHash);
        String paymentUrl = Config.vnp_PayUrl + "?" + query.toString();

        System.out.println("[VNPAY] Redirect to payment: " + paymentUrl);
        resp.sendRedirect(paymentUrl);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doPost(req, resp);
    }
}
