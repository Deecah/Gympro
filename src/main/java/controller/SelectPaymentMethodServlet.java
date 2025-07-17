package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/SelectPaymentMethodServlet")
public class SelectPaymentMethodServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String method = request.getParameter("method");
            int packageId = Integer.parseInt(request.getParameter("packageId"));
            int trainerId = Integer.parseInt(request.getParameter("trainerId"));
            int duration = Integer.parseInt(request.getParameter("duration"));
            String amount = request.getParameter("amount");

            HttpSession session = request.getSession();

            session.setAttribute("selectedPackageId", packageId);
            session.setAttribute("selectedTrainerId", trainerId);
            session.setAttribute("selectedPackageDuration", duration);
            session.setAttribute("selectedPackageAmount", amount);
            session.setAttribute("contractType", "package");

            Integer customerId = (Integer) session.getAttribute("userId");
            if (customerId != null) {
                session.setAttribute("loggedInCustomerId", customerId);
            }

            switch (method) {
                case "paypal":
                    request.getRequestDispatcher("paypal/create-payment").forward(request, response);
                    return;
                case "vnpay":
                    request.getRequestDispatcher("vnpay/create-payment").forward(request, response);
                    return;
                case "payos":
                    request.getRequestDispatcher("payos/create-payment").forward(request, response);
                    return;
                default:
                    response.sendRedirect("payment-fail.jsp");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("payment-fail.jsp");
        }
    }
}
