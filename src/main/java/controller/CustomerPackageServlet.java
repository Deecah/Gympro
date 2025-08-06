
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

        int page = 1;
        int pageSize = 6;
        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            try {
                page = Integer.parseInt(pageParam);
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        PackageDAO dao = new PackageDAO();
        int totalItems = dao.countAllPackages();
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);
        int offset = (page - 1) * pageSize;
        List<Package> packages = dao.getPackagesByPage(pageSize, offset);
        request.setAttribute("packages", packages);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);

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
