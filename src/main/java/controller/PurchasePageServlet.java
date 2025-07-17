package controller;

import dao.PackageDAO;
import model.Package;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/purchase")
public class PurchasePageServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String rawId = request.getParameter("packageId");
            if (rawId == null) {
                response.getWriter().println("Error: Missing packageId parameter.");
                return;
            }

            int packageId = Integer.parseInt(rawId);
            PackageDAO dao = new PackageDAO();
            Package p = dao.getPackageById(packageId);

            if (p != null) {
                request.setAttribute("pkg", p);
                request.getRequestDispatcher("purchase.jsp").forward(request, response);
            } else {
                response.sendRedirect("not-found.jsp");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Error: " + e.getMessage());
        }
    }
}
