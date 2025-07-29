
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
        try {
            ReportDAO reportDAO = new ReportDAO();
            UserDAO userDAO = new UserDAO();
            
            // Lấy danh sách báo cáo
            ArrayList<ViolationReport> reportList = reportDAO.getAllReports();
            
            // Debug: In ra số lượng báo cáo
            System.out.println("Số lượng báo cáo: " + (reportList != null ? reportList.size() : "null"));
            
            // Tạo map để lưu thông tin user
            HashMap<Integer, User> mapUser = new HashMap<>();
            
            if (reportList != null && !reportList.isEmpty()) {
                for (ViolationReport report : reportList) {
                    // Lấy thông tin người bị báo cáo
                    int reportedUserId = report.getReportedUserID();
                    if (!mapUser.containsKey(reportedUserId)) {
                        User reportedUser = userDAO.getUserById(reportedUserId);
                        if (reportedUser != null) {
                            mapUser.put(reportedUserId, reportedUser);
                            System.out.println("Đã thêm user bị báo cáo: " + reportedUser.getUserName());
                        } else {
                            System.out.println("Không tìm thấy user bị báo cáo với ID: " + reportedUserId);
                        }
                    }
                    
                    // Lấy thông tin người báo cáo
                    int fromUserId = report.getFromUserID();
                    if (!mapUser.containsKey(fromUserId)) {
                        User fromUser = userDAO.getUserById(fromUserId);
                        if (fromUser != null) {
                            mapUser.put(fromUserId, fromUser);
                            System.out.println("Đã thêm user báo cáo: " + fromUser.getUserName());
                        } else {
                            System.out.println("Không tìm thấy user báo cáo với ID: " + fromUserId);
                        }
                    }
                }
            }
            
            // Debug: In ra số lượng user trong map
            System.out.println("Số lượng user trong map: " + mapUser.size());
            
            // Set attributes
            request.setAttribute("mapUser", mapUser);
            request.setAttribute("reportList", reportList);
            
            // Forward đến JSP
            request.getRequestDispatcher("/adminDashboard/viewreport.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            // Redirect về trang lỗi hoặc hiển thị thông báo lỗi
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Có lỗi xảy ra: " + e.getMessage());
        }
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