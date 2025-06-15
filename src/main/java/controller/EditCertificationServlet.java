/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import dao.CertificationDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Certification;

public class EditCertificationServlet extends HttpServlet {
    private CertificationDAO certificationDAO;

    @Override
    public void init() {
        certificationDAO = new CertificationDAO();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet EditCertificationServlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet EditCertificationServlet at " + request.getContextPath () + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    } 

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        int certId = Integer.parseInt(request.getParameter("id"));

        try {
            Certification cert = certificationDAO.getCertificationById(certId);
            request.setAttribute("cert", cert);
            request.getRequestDispatcher("editCertification.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("viewCertification.jsp?error=NotFound");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(EditCertificationServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        try {
            int certId = Integer.parseInt(request.getParameter("certificationId"));
            String title = request.getParameter("title");
            String org = request.getParameter("organization");
            Date issue = Date.valueOf(request.getParameter("issueDate"));
            Date expiry = Date.valueOf(request.getParameter("expiryDate"));
            String description = request.getParameter("description");
            String fileUrl = request.getParameter("fileUrl");

            Certification cert = new Certification();
            cert.setCertificationId(certId);
            cert.setTitle(title);
            cert.setOrganization(org);
            cert.setIssueDate(issue);
            cert.setExpiryDate(expiry);
            cert.setDescription(description);
            cert.setFileUrl(fileUrl);

            certificationDAO.updateCertification(cert);

            response.sendRedirect("viewCertification.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Update failed");
            request.getRequestDispatcher("editCertification.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
