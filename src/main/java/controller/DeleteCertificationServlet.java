package controller;

import dao.CertificationDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "DeleteCertificationServlet", urlPatterns = {"/DeleteCertificationServlet"})
public class DeleteCertificationServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        String idStr = request.getParameter("id");

        try {
            int id = Integer.parseInt(idStr);
            CertificationDAO dao = new CertificationDAO();
            dao.deleteCertification(id);

            // Redirect back to the certification list page
            response.sendRedirect("ViewCertificationServlet");
        } catch (NumberFormatException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");  // Optional: Create an error page
        }
    }
}
