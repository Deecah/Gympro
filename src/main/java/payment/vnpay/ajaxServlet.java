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
import model.User;
import com.google.gson.JsonObject;
import com.google.gson.Gson;

@WebServlet(name = "ajaxServlet", urlPatterns = {"/vnpay/payment"})
public class ajaxServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            // Check if user is logged in
            HttpSession session = request.getSession();
            User currentUser = (User) session.getAttribute("user");

            if (currentUser == null) {
                JsonObject errorResponse = new JsonObject();
                errorResponse.addProperty("code", "01");
                errorResponse.addProperty("message", "Vui lòng đăng nhập để thực hiện thanh toán");
                response.getWriter().write(new Gson().toJson(errorResponse));
                return;
            }

            // Get payment parameters
            String vnp_Version = "2.1.0";
            String vnp_Command = "pay";
            String orderType = "other";

            // Get amount from request and convert to VNPay format (multiply by 100)
            String amountStr = request.getParameter("amount");
            if (amountStr == null || amountStr.trim().isEmpty()) {
                JsonObject errorResponse = new JsonObject();
                errorResponse.addProperty("code", "02");
                errorResponse.addProperty("message", "Số tiền không hợp lệ");
                response.getWriter().write(new Gson().toJson(errorResponse));
                return;
            }

            long amount = (long) (Double.parseDouble(amountStr) * 100);
            String bankCode = request.getParameter("bankCode");

            // Generate transaction reference
            String vnp_TxnRef = Config.getRandomNumber(8);
            String vnp_IpAddr = Config.getIpAddress(request);
            String vnp_TmnCode = Config.vnp_TmnCode;

            // Build VNPay parameters
            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Version", vnp_Version);
            vnp_Params.put("vnp_Command", vnp_Command);
            vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
            vnp_Params.put("vnp_Amount", String.valueOf(amount));
            vnp_Params.put("vnp_CurrCode", "VND");

            if (bankCode != null && !bankCode.isEmpty()) {
                vnp_Params.put("vnp_BankCode", bankCode);
            }

            vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
            vnp_Params.put("vnp_OrderInfo", "Nap tien vao vi RentEz - User: " + currentUser.getUserName() + " - TxnRef: " + vnp_TxnRef);
            vnp_Params.put("vnp_OrderType", orderType);

            // Set locale
            String locale = request.getParameter("language");
            if (locale != null && !locale.isEmpty()) {
                vnp_Params.put("vnp_Locale", locale);
            } else {
                vnp_Params.put("vnp_Locale", "vn");
            }

            vnp_Params.put("vnp_ReturnUrl", Config.vnp_ReturnUrl);
            vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

            // Set create date and expire date
            Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String vnp_CreateDate = formatter.format(cld.getTime());
            vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

            cld.add(Calendar.MINUTE, 15); // Expire after 15 minutes
            String vnp_ExpireDate = formatter.format(cld.getTime());
            vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

            // Build hash data and query string
            List fieldNames = new ArrayList(vnp_Params.keySet());
            Collections.sort(fieldNames);
            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();
            Iterator itr = fieldNames.iterator();

            while (itr.hasNext()) {
                String fieldName = (String) itr.next();
                String fieldValue = (String) vnp_Params.get(fieldName);
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    // Build hash data
                    hashData.append(fieldName);
                    hashData.append('=');
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString())
                    );
                    // Build query
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString())
                    );
                    query.append('=');
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString())
                    );
                    if (itr.hasNext()) {
                        query.append('&');
                        hashData.append('&');
                    }
                }
            }

            String queryUrl = query.toString();
            String vnp_SecureHash = Config.hmacSHA512(Config.secretKey, hashData.toString());
            queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
            String paymentUrl = Config.vnp_PayUrl + "?" + queryUrl;

            // Store transaction info in session for later verification
            session.setAttribute("vnpay_txnref", vnp_TxnRef);
            session.setAttribute("vnpay_amount", amountStr);
            session.setAttribute("vnpay_user_id", currentUser.getUserId());

            // Return success response with payment URL
//            JsonObject successResponse = new JsonObject();
//            successResponse.addProperty("code", "00");
//            successResponse.addProperty("message", "success");
//            successResponse.addProperty("data", paymentUrl);
//            
//            Gson gson = new Gson();
//            response.getWriter().write(gson.toJson(successResponse));
            response.sendRedirect(paymentUrl);

        } catch (Exception e) {
            e.printStackTrace();
            JsonObject errorResponse = new JsonObject();
            errorResponse.addProperty("code", "99");
            errorResponse.addProperty("message", "Có lỗi xảy ra: " + e.getMessage());
            response.getWriter().write(new Gson().toJson(errorResponse));
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doPost(req, resp);
    }
}
