
package controller;

import dao.BlogDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.Blog;

    @WebServlet(name="BlogServlet", urlPatterns={"/BlogServlet"})
public class BlogServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        int pageSize = 9;
        int currentPage = 1;

        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            try {
                currentPage = Integer.parseInt(pageParam);
            } catch (NumberFormatException e) {
                currentPage = 1;
            }
        }

        BlogDAO blogDAO = new BlogDAO();
        List<Blog> allBlogs = blogDAO.getAllBlogs();

        int totalBlogs = allBlogs.size();
        int totalPages = (int) Math.ceil((double) totalBlogs / pageSize);

        int startIndex = (currentPage - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, totalBlogs);
        List<Blog> blogsToShow = allBlogs.subList(startIndex, endIndex);

        // Gửi dữ liệu lên JSP
        request.setAttribute("blogs", blogsToShow);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);

        request.getRequestDispatcher("/blog.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
         
    }

}