
package controller.admin;

import dao.ReportDAO;
import dao.UserDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import model.User;
import model.ViolationReport;

@WebServlet(name="ReportServlet", urlPatterns={"/ReportServlet"})
public class ReportServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ViewReportServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ViewReportServlet at " + request.getContextPath () + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        ReportDAO reportDAO = new ReportDAO();
        ArrayList<ViolationReport> reportList = reportDAO.getAllReports();
        HashMap<Integer,User> mapUser = new HashMap();
        for(ViolationReport r : reportList){    // vong loop de lay cac user lien quan toi reports
            int id = r.getReportedUserID();
            if(!mapUser.containsKey(id)){
                UserDAO userDAO = new UserDAO();
                User u = userDAO.getUserById(id);
                mapUser.put(id, u);
            }
            id = r.getFromUserID();
            if(!mapUser.containsKey(id)){
                UserDAO userDAO = new UserDAO();
                User u = userDAO.getUserById(id);
                mapUser.put(id, u);
            }
        }
        request.setAttribute("mapUser", mapUser);
        request.setAttribute("reportList", reportList);
        request.getRequestDispatcher("/adminDashboard/viewreport.jsp").forward(request, response);
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