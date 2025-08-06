
package controller;

import dao.PackageDAO;
import dao.TrainerDAO;
import model.Trainer;
import model.Package;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.Trainer;

public class SearchServlet extends HttpServlet {
    private TrainerDAO trainerDAO;
    private PackageDAO packageDAO;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet SearchServlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet SearchServlet at " + request.getContextPath () + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }
    @Override
    public void init() throws ServletException {
        trainerDAO  = new TrainerDAO();
        packageDAO  = new PackageDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Lấy từ khoá
        String keyword = request.getParameter("keyword");
        if (keyword == null) keyword = "";         // tránh NPE

        // 2. Tìm Trainer & Package
        List<Trainer> trainers  = trainerDAO.searchByKeyword(keyword);
        List<Package> packages  = packageDAO.searchByKeyword(keyword);
        
        // 3. Đặt thuộc tính và forward sang JSP
        request.setAttribute("keyword",  keyword);
        request.setAttribute("trainers", trainers);
        request.setAttribute("packages", packages);

        request.getRequestDispatcher("packages.jsp").forward(request, response);
    } 
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}

