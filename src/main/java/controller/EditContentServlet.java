package controller;

import dao.ContentDAO;
import model.Content;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/EditContentServlet")
public class EditContentServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int id = Integer.parseInt(request.getParameter("contentId"));
            String title = request.getParameter("contentTitle");
            String body = request.getParameter("contentBody");

            Content updated = new Content(id, title, body);

            boolean success = ContentDAO.update(updated);

            if (success) {
                response.sendRedirect("contentList.jsp"); // redirect nếu cập nhật thành công
            } else {
                response.getWriter().println("Failed to update content.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Error: " + e.getMessage());
        }
    }
}
