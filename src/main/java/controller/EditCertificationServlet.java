package controller;
import dao.CertificationDAO;
import model.Certification;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

public class EditCertificationServlet extends HttpServlet {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int certificationID = Integer.parseInt(request.getParameter("certificationID"));
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            String expireDateStr = request.getParameter("expireDate");

            LocalDateTime expireDate = LocalDateTime.parse(expireDateStr, formatter);

            Certification cert = new Certification();
            cert.setCertificationID(certificationID);
            cert.setName(name);
            cert.setDescription(description);
            cert.setExpireDate(expireDate);

            CertificationDAO dao = new CertificationDAO();
            dao.updateCertification(cert);

            response.sendRedirect("viewCertification.jsp");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error updating certification: " + e.getMessage());
            request.getRequestDispatcher("editCertification.jsp").forward(request, response);
        }
    }
}
