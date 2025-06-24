package controller;

import dao.CertificationDAO;
import model.Certification;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;


public class AddCertificationServlet extends HttpServlet {

    // Format tương ứng với input type="datetime-local"
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            String expireDateStr = request.getParameter("expireDate");

            // Parse ngày hết hạn từ form
            LocalDateTime expireDate = LocalDateTime.parse(expireDateStr, formatter);

            Certification certification = new Certification();
            certification.setName(name);
            certification.setDescription(description);
            certification.setExpireDate(expireDate);

            CertificationDAO dao = new CertificationDAO();
            dao.addCertification(certification);

            // Chuyển hướng đến trang xem danh sách (bạn có thể đổi URL nếu cần)
            response.sendRedirect("ViewCertificationServlet");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi khi thêm chứng chỉ: " + e.getMessage());
            request.getRequestDispatcher("addCertification.jsp").forward(request, response);
        }
    }
}