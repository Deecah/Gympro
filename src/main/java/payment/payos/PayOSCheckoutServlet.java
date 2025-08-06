package payment.payos;

import dao.TransactionDAO;
import model.Transaction;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@WebServlet("/payos/checkout")
public class PayOSCheckoutServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            HttpSession session = request.getSession();
            Integer customerId = (Integer) session.getAttribute("loggedInCustomerId");
            Integer contractId = (Integer) session.getAttribute("contractId");
            int trainerId = Integer.parseInt(request.getParameter("trainerId"));
            int packageId = Integer.parseInt(request.getParameter("packageId"));
            int duration = Integer.parseInt(request.getParameter("duration"));
            // Sửa lỗi parseInt: dùng double, ép kiểu int cho PayOS
            double amount = Double.parseDouble(request.getParameter("amount"));
            int amountInt = (int) Math.round(amount); // PayOS chỉ nhận số nguyên VND

            session.setAttribute("selectedTrainerId", trainerId);
            session.setAttribute("selectedPackageId", packageId);
            session.setAttribute("selectedPackageDuration", duration);

            Transaction transaction = new Transaction();
            transaction.setContractId(contractId);
            transaction.setAmount(new java.math.BigDecimal(amount)); // Lưu đúng số tiền thực tế
            transaction.setStatus("Processing");
            transaction.setCreatedTime(Timestamp.valueOf(LocalDateTime.now()));
            transaction.setDescription("Thanh toán qua PayOS - Gói tập GymPro");
            transaction.setCustomerId(customerId);

            TransactionDAO transactionDAO = new TransactionDAO();
            int transactionId = transactionDAO.addTransaction(transaction);  
            System.out.println("[PayOS] transactionId: " + transactionId);

            PayOSUtil.PayOSResponse payosResp = payment.payos.PayOSUtil.createPaymentRequest(
            transactionId, amountInt, "Thanh toán gói tập GymPro");
            System.out.println("[PayOS] PayOSUtil.PayOSResponse: " + payosResp.rawResponse);

            if (payosResp.checkoutUrl != null && !payosResp.checkoutUrl.isEmpty()) {
                System.out.println("[PayOS] checkoutUrl: " + payosResp.checkoutUrl);
                response.sendRedirect(payosResp.checkoutUrl);
            } else {
                String errorMsg = "Không thể khởi tạo thanh toán PayOS.\n" + (payosResp.error != null ? payosResp.error : "") + "\nRaw: " + payosResp.rawResponse;
                request.setAttribute("error", errorMsg);
                request.getRequestDispatcher("/error.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Đã xảy ra lỗi khi khởi tạo thanh toán: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    private String extractCheckoutUrl(String json) {
        try {
            int start = json.indexOf("\"checkoutUrl\":\"") + 15;
            int end = json.indexOf("\"", start);
            String url = json.substring(start, end);
            return url.replace("\\/", "/");
        } catch (Exception e) {
            return null;
        }
    }
}
