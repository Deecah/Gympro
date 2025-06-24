package controller;

import dao.CertificationDAO;
import model.Certification;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class EditCertificationServlet extends HttpServlet {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int certId = Integer.parseInt(request.getParameter("id"));
            CertificationDAO dao = new CertificationDAO();
            Certification cert = dao.getCertificationByID(certId);

            if (cert != null) {
                request.setAttribute("cert", cert);
                request.getRequestDispatcher("editCertification.jsp").forward(request, response);
            } else {
                response.sendRedirect("ViewCertificationServlet"); // Nếu không tìm thấy, quay lại danh sách
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("ViewCertificationServlet"); // fallback nếu lỗi
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int id = Integer.parseInt(request.getParameter("certificationID"));
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            String expireDateStr = request.getParameter("expireDate");

            LocalDateTime expireDate = LocalDateTime.parse(expireDateStr, formatter);

            Certification cert = new Certification();
            cert.setCertificationID(id);
            cert.setName(name);
            cert.setDescription(description);
            cert.setExpireDate(expireDate);

            CertificationDAO dao = new CertificationDAO();
            dao.updateCertification(cert);

            response.sendRedirect("ViewCertificationServlet");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi khi cập nhật chứng chỉ: " + e.getMessage());
            request.getRequestDispatcher("editCertification.jsp").forward(request, response);
        }
    }
}
