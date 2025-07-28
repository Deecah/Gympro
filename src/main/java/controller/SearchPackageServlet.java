package controller;

import dao.PackageDAO;
import model.Package;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "SearchPackageServlet", urlPatterns = {"/searchPackage"})
public class SearchPackageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Lấy từ khóa tìm kiếm từ request
        String keyword = request.getParameter("keyword");

        // Gọi DAO để tìm kiếm gói tập theo từ khóa
        PackageDAO dao = new PackageDAO();
        List<Package> list = null;
        if (keyword != null && !keyword.trim().isEmpty()) {
            list = dao.searchByKeyword(keyword.trim());
        }

        // Gửi dữ liệu sang JSP
        request.setAttribute("packages", list);
        request.setAttribute("keyword", keyword); // để giữ lại giá trị ô input khi reload
        request.getRequestDispatcher("packages.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response); // Có thể dùng chung nếu form POST
    }

    @Override
    public String getServletInfo() {
        return "Handles search functionality for training packages.";
    }
}

