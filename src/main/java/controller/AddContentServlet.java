/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.ContentDAO;
import model.Content;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "AddContentServlet", urlPatterns = {"/AddContentServlet"})
public class AddContentServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8"); // để đọc tiếng Việt

        String title = request.getParameter("contentTitle");
        String body = request.getParameter("contentBody");

        Content newContent = new Content(title, body);
        boolean success = ContentDAO.insert(newContent);

        if (success) {
            response.sendRedirect("contentList.jsp");  // ✅ Thành công thì chuyển trang
        } else {
            request.setAttribute("error", "Insert failed");
            request.getRequestDispatcher("adminDashboard.jsp").forward(request, response); // ❌ nếu lỗi
        }
    }

}
