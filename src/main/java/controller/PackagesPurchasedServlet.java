package controller;

import connectDB.ConnectDatabase;
import dao.PackageDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Package;
import model.PackageDTO;

import java.io.IOException;
import java.sql.*;
import java.util.*;

@WebServlet(name = "PackagesPurchasedServlet", urlPatterns = {"/packagesPurchased"})
public class PackagesPurchasedServlet extends HttpServlet {
    private PackageDAO packageDAO = new PackageDAO();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        HttpSession session = request.getSession();
        String userId = null;
        String role = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equalsIgnoreCase("userId") && cookie.getValue() != null) {
                userId = cookie.getValue();
            } else if (cookie.getName().equalsIgnoreCase("role") && cookie.getValue() != null) {
                role = cookie.getValue();
            }
        }
        if (userId == null || role == null || !role.equalsIgnoreCase("Customer")) {
            response.sendRedirect("login.jsp");
            return;
        }

        List<PackageDTO> packageList = packageDAO.getPurchasedPackagesByCustomer(Integer.parseInt(userId));
        Boolean feedbackSuccess = (Boolean) request.getSession().getAttribute("feedbackSuccess");
        if (feedbackSuccess != null && feedbackSuccess) {
            request.setAttribute("feedbackSuccess", true);
            request.getSession().removeAttribute("feedbackSuccess"); // prevent showing again on refresh
        }
        request.setAttribute("purchasedList", packageList);
        request.getRequestDispatcher("packagesPurchased.jsp").forward(request, response);
    }
}
