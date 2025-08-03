package controller;

import dao.ContentDAO;
import model.Content;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/ContentServlet")
public class ContentServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        try {
            switch (action) {

                case "submitAdd": {
                    // Xử lý khi form addContent.jsp được submit
                    String title = request.getParameter("contentTitle");
                    String body = request.getParameter("contentBody");

                    Content newContent = new Content(title, body);
                    boolean success = ContentDAO.insert(newContent);

                    if (success) {
                        response.sendRedirect(request.getContextPath() + "/ContentServlet?action=viewAll");
                    } else {
                        request.setAttribute("error", "Insert failed");
                        request.getRequestDispatcher("/adminDashboard/addContent.jsp").forward(request, response);
                    }
                    break;
                }

                case "submitEdit": {
                    int id = Integer.parseInt(request.getParameter("contentId"));
                    String title = request.getParameter("contentTitle");
                    String body = request.getParameter("contentBody");

                    Content updated = new Content(id, title, body);
                    boolean success = ContentDAO.update(updated);

                    if (success) {
                        response.sendRedirect(request.getContextPath() + "/ContentServlet?action=viewAll");
                    } else {
                        request.setAttribute("error", "Update failed");
                        request.getRequestDispatcher("/adminDashboard/editContent.jsp").forward(request, response);
                    }
                    break;
                }

                default:
                    request.setAttribute("error", "Invalid action in POST");
                    request.getRequestDispatcher("/adminDashboard/viewContent.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error: " + e.getMessage());
            request.getRequestDispatcher("/adminDashboard/viewContent.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        try {
            switch (action) {
                case "view": {
                    int id = Integer.parseInt(request.getParameter("contentId"));
                    Content content = ContentDAO.getById(id);

                    if (content != null) {
                        request.setAttribute("content", content);
                        request.getRequestDispatcher("/adminDashboard/viewContentDetail.jsp").forward(request, response);
                    } else {
                        request.setAttribute("error", "Content not found");
                        request.getRequestDispatcher("/adminDashboard/viewContent.jsp").forward(request, response);
                    }
                    break;
                }
                case "delete": {
                    int id = Integer.parseInt(request.getParameter("id"));
                    boolean deleted = ContentDAO.delete(id);

                    if (deleted) {
                        response.sendRedirect(request.getContextPath() + "/ContentServlet?action=viewAll");
                    } else {
                        request.setAttribute("error", "Delete failed");
                        request.getRequestDispatcher("/adminDashboard/viewContent.jsp").forward(request, response);
                    }
                    break;
                }
                case "viewAll": {
                    List<Content> contents = ContentDAO.getAll();

                    request.setAttribute("contents", contents);
                    request.getRequestDispatcher("/adminDashboard/viewContent.jsp").forward(request, response);
                    break;
                }

                case "add": {
                    // Chuyển đến trang addContent.jsp
                    request.getRequestDispatcher("/adminDashboard/addContent.jsp").forward(request, response);
                    break;
                }

                case "editForm": {
                    int id = Integer.parseInt(request.getParameter("contentId"));
                    Content content = ContentDAO.getById(id);

                    if (content != null) {
                        request.setAttribute("content", content);
                        request.getRequestDispatcher("/adminDashboard/editContent.jsp").forward(request, response);
                    } else {
                        request.setAttribute("error", "Content not found");
                        request.getRequestDispatcher("/adminDashboard/viewContent.jsp").forward(request, response);
                    }
                    break;
                }

                default:
                    request.setAttribute("error", "Invalid action in GET");
                    request.getRequestDispatcher("/adminDashboard/viewContent.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error: " + e.getMessage());
            request.getRequestDispatcher("/adminDashboard/viewContent.jsp").forward(request, response);
        }
    }
}
