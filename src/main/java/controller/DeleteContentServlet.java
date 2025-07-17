package controller;

import dao.ContentDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/DeleteContentServlet")  // <- Đây là đường dẫn mapping thay cho web.xml
public class DeleteContentServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int contentId = Integer.parseInt(request.getParameter("contentId"));
            boolean deleted = ContentDAO.delete(contentId);

            if (deleted) {
                response.sendRedirect("contentList.jsp"); // hoặc LoadContentListServlet
            } else {
                request.setAttribute("error", "Delete failed.");
                request.getRequestDispatcher("adminDashboard.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Invalid ID or internal error.");
            request.getRequestDispatcher("adminDashboard.jsp").forward(request, response);
        }
    }
}
