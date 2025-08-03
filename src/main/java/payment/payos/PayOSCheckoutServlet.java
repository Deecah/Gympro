package payment.payos;

import dao.TransactionDAO;
import model.Transaction;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet(name = "PayOSCheckoutServlet", urlPatterns = {"/payos/checkout"})

public class PayOSCheckoutServlet extends HttpServlet {

   @Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    try {
        HttpSession session = request.getSession();
        Integer customerId = (Integer) session.getAttribute("loggedInCustomerId");

        System.out.println("[DEBUG] loggedInCustomerId = " + customerId);
        if (customerId == null) {
            throw new IllegalStateException("Customer is not logged in.");
        }

        int trainerId = Integer.parseInt(request.getParameter("trainerId"));
        int packageId = Integer.parseInt(request.getParameter("packageId"));
        int duration = Integer.parseInt(request.getParameter("duration"));
        double amount = Double.parseDouble(request.getParameter("amount"));
        int amountInt = (int) Math.round(amount); // PayOS chỉ nhận số nguyên

        System.out.println("[DEBUG] Request Params: trainerId=" + trainerId
            + ", packageId=" + packageId + ", duration=" + duration
            + ", amount(double)=" + amount + ", amountInt=" + amountInt);

        session.setAttribute("selectedTrainerId", trainerId);
        session.setAttribute("selectedPackageId", packageId);
        session.setAttribute("selectedPackageDuration", duration);

        Transaction transaction = new Transaction();
        transaction.setCustomerId(customerId);
        transaction.setAmount(BigDecimal.valueOf(amount));
        transaction.setStatus("Processing");
        transaction.setType("PayOS");
        transaction.setDescription("Thanh toán qua PayOS - Gói tập GymPro");

        TransactionDAO transactionDAO = new TransactionDAO();
        int transactionId = transactionDAO.addTransaction(transaction);
        System.out.println("[PayOS] Created transactionId: " + transactionId);

        PayOSUtil.PayOSResponse payosResp = PayOSUtil.createPaymentRequest(
            transactionId, amountInt, "Thanh toán gói tập GymPro");

        System.out.println("[PayOS] Raw response from PayOS:");
        System.out.println(payosResp.rawResponse);

        if (payosResp.error != null) {
            System.out.println("[PayOS] ERROR: " + payosResp.error);
        }

        if (payosResp.checkoutUrl != null && !payosResp.checkoutUrl.isEmpty()) {
            System.out.println("[PayOS] Redirecting to checkoutUrl: " + payosResp.checkoutUrl);
            response.sendRedirect(payosResp.checkoutUrl);
        } else {
            String errorMsg = "Không thể khởi tạo thanh toán PayOS.\n" +
                (payosResp.error != null ? payosResp.error : "") +
                "\nRaw: " + payosResp.rawResponse;

            System.out.println("[PayOS] Failed to create payment. Error message:");
            System.out.println(errorMsg);

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
