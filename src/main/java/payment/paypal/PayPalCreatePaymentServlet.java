package payment.paypal;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import Utils.ExchangeRateUtil;

@WebServlet("/paypal/create-payment")
public class PayPalCreatePaymentServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            HttpSession session = request.getSession();

            int customerId = (int) session.getAttribute("loggedInCustomerId");
            int trainerId = (int) session.getAttribute("selectedTrainerId");
            int packageId = (int) session.getAttribute("selectedPackageId");
            int duration = (int) session.getAttribute("selectedPackageDuration");
            String vndAmountStr = (String) session.getAttribute("selectedPackageAmount");
            BigDecimal vndAmount = new BigDecimal(vndAmountStr);
            BigDecimal exchangeRate = ExchangeRateUtil.fetchVNDToUSD();
            BigDecimal usdAmount = vndAmount.divide(exchangeRate, 2, RoundingMode.HALF_UP);
            String contractType = (String) session.getAttribute("contractType");

            Amount payAmount = new Amount("USD", usdAmount.toPlainString());
            Transaction transaction = new Transaction();
            transaction.setAmount(payAmount);
            transaction.setDescription("GymPro package payment");

            Payer payer = new Payer();
            payer.setPaymentMethod("paypal");

            RedirectUrls redirectUrls = new RedirectUrls();
            redirectUrls.setCancelUrl("http://localhost:8080/SWP391/payment-fail.jsp");
            redirectUrls.setReturnUrl("http://localhost:8080/SWP391/paypal/execute-payment");

            Payment payment = new Payment();
            payment.setIntent("sale");
            payment.setPayer(payer);
            payment.setRedirectUrls(redirectUrls);
            payment.setTransactions(Collections.singletonList(transaction));

            APIContext apiContext = PayPalConfig.getAPIContext();
            Payment createdPayment = payment.create(apiContext);

            for (Links link : createdPayment.getLinks()) {
                if ("approval_url".equals(link.getRel())) {
                    response.sendRedirect(link.getHref());
                    return;
                }
            }

            response.sendRedirect("payment-fail.jsp");

        } catch (Exception ex) {
            ex.printStackTrace();
            response.sendRedirect("payment-fail.jsp");
        }
    }
}