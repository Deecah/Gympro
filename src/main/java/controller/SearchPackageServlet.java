package controller;

import dao.PackageDAO;
import model.Package;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

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
        request.setAttribute("packageList", list);
        request.setAttribute("keyword", keyword); // để giữ lại giá trị ô input khi reload
        request.getRequestDispatcher("searchPackage.jsp").forward(request, response);
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
