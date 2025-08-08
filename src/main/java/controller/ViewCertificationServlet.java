    /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.CertificationDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.util.List;
import model.Certification;

@WebServlet(name = "ViewCertificationServlet", urlPatterns = {"/ViewCertificationServlet"})
public class ViewCertificationServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        CertificationDAO dao = new CertificationDAO();
        List<Certification> certList = dao.getAllCertifications();

        // Gửi danh sách sang JSP
        request.setAttribute("certList", certList);

        // Chuyển tiếp sang trang JSP để hiển thị
        request.getRequestDispatcher("viewCertifications.jsp").forward(request, response);
    }
}
