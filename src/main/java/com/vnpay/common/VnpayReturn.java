package com.vnpay.common;

import dao.TransactionDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import model.Transaction;

public class VnpayReturn extends HttpServlet {

    TransactionDAO transacsionDAO = new TransactionDAO();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            Map fields = new HashMap();
            for (Enumeration params = request.getParameterNames(); params.hasMoreElements();) {
                String fieldName = URLEncoder.encode((String) params.nextElement(), StandardCharsets.US_ASCII.toString());
                String fieldValue = URLEncoder.encode(request.getParameter(fieldName), StandardCharsets.US_ASCII.toString());
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    fields.put(fieldName, fieldValue);
                }
            }

            String vnp_SecureHash = request.getParameter("vnp_SecureHash");
            fields.remove("vnp_SecureHashType");
            fields.remove("vnp_SecureHash");

            String signValue = Config.hashAllFields(fields);
            if (signValue.equals(vnp_SecureHash)) {
                String transactionId = request.getParameter("vnp_TxnRef");
                String transactionStatus = request.getParameter("vnp_TransactionStatus");

                Transaction transaction = new Transaction();
                transaction.setId(Integer.parseInt(transactionId));

                boolean transSuccess = false;
                if ("00".equals(transactionStatus)) {
                    transaction.setStatus("Completed");
                    transSuccess = true;
                } else {
                    transaction.setStatus("Failed");
                }
//                transacsionDAO.updateTransactionStatus(transaction);

                request.setAttribute("transResult", transSuccess);
                request.getRequestDispatcher("paymentResult.jsp").forward(request, response);
            } else {
                System.out.println("Giao dịch không hợp lệ (Invalid signature)");
            }
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

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
