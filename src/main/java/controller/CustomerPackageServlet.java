
package controller;

import dao.ContractDAO;
import dao.PackageDAO;
import model.Package;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@WebServlet(name = "CustomerPackageServlet", urlPatterns = {"/CustomerPackageServlet"})
public class CustomerPackageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        PackageDAO dao = new PackageDAO();
        List<Package> packages = dao.getAllPackages();
        request.setAttribute("packages", packages);

        HttpSession session = request.getSession(false);
        Integer customerId = (session != null) ? (Integer) session.getAttribute("userId") : null;

        Map<Integer, Boolean> purchaseStatus = new HashMap<>();
        if (customerId != null) {
            ContractDAO contractDAO = new ContractDAO();
            for (Package p : packages) {
                boolean isActive = contractDAO.isPackageActiveForCustomer(customerId, p.getPackageID());
                purchaseStatus.put(p.getPackageID(), isActive);
            }
        }

        request.setAttribute("purchaseStatus", purchaseStatus);
        RequestDispatcher dispatcher = request.getRequestDispatcher("packages.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
