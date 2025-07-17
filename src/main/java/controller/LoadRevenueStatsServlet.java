/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.TransactionDAO;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Map;

@WebServlet(name = "LoadRevenueStatsServlet", urlPatterns = {"/loadRevenueStats"})
public class LoadRevenueStatsServlet extends HttpServlet {

    private final TransactionDAO transactionDAO = new TransactionDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Map<String, BigDecimal> revenueMap = transactionDAO.getMonthlyRevenue();

        String json = new Gson().toJson(revenueMap);
        System.out.println("DEBUG: Servlet doGet called");
        System.out.println("DEBUG: JSON = " + json); // <== dòng này in ra JSON

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        PrintWriter out = resp.getWriter();
        out.print(json);
        out.flush();
    }

}
