package controller.admin;

import dao.UserDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import model.User;

@WebServlet(name = "UserServlet", urlPatterns = {"/UserServlet"})
public class UserServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "initial_load"; // Mặc định hiển thị user list hoặc dashboard
        }

        switch (action) {
            case "banuser":
                handleBanUser(request, response);
                break;
            case "unbanuser":
                handleUnbanUser(request, response);
                break;
            case "view": {
                UserDAO userDAO = new UserDAO();
                ArrayList<User> userList;
                String viewType = request.getParameter("type");
                String message = request.getParameter("message");
                
                if (viewType == null || viewType.isEmpty() || viewType.equals("all")) {
                    userList = userDAO.getAllUsers();
                } else if (viewType.equals("Trainer")) {
                    userList = userDAO.getUsersByRole("Trainer");
                } else if (viewType.equals("Customer")) {
                    userList = userDAO.getUsersByRole("Customer");
                } else {
                    userList = userDAO.getAllUsers();
                }
                
                request.setAttribute("userList", userList);
                request.setAttribute("selectedType", viewType != null ? viewType : "all");
                request.setAttribute("message", message);
                request.getRequestDispatcher("/adminDashboard/viewuser.jsp").forward(request, response);
                break;
            }
            default: {
                UserDAO userDAO = new UserDAO();
                ArrayList<User> userList = userDAO.getAllUsers();
                request.setAttribute("userList", userList);
                request.setAttribute("selectedType", "all");
                request.getRequestDispatcher("/adminDashboard/viewuser.jsp").forward(request, response);
                break;
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    public void handleBanUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int userId = Integer.parseInt(request.getParameter("userId"));
            UserDAO userDAO = new UserDAO();
            boolean success = userDAO.banUser(userId);
            
            // Redirect về trang viewuser với thông báo
            String redirectUrl = "UserServlet?action=view";
            String currentType = request.getParameter("currentType");
            if (currentType != null && !currentType.isEmpty()) {
                redirectUrl += "&type=" + currentType;
            }
            redirectUrl += "&message=" + (success ? "ban_success" : "ban_failed");
            
            response.sendRedirect(redirectUrl);
        } catch (NumberFormatException e) {
            response.sendRedirect("UserServlet?action=view&message=invalid_id");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("UserServlet?action=view&message=server_error");
        }
    }

    public void handleUnbanUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int userId = Integer.parseInt(request.getParameter("userId"));
            UserDAO userDAO = new UserDAO();
            boolean success = userDAO.unbanUser(userId);
            
            // Redirect về trang viewuser với thông báo
            String redirectUrl = "UserServlet?action=view";
            String currentType = request.getParameter("currentType");
            if (currentType != null && !currentType.isEmpty()) {
                redirectUrl += "&type=" + currentType;
            }
            redirectUrl += "&message=" + (success ? "unban_success" : "unban_failed");
            
            response.sendRedirect(redirectUrl);
        } catch (NumberFormatException e) {
            response.sendRedirect("UserServlet?action=view&message=invalid_id");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("UserServlet?action=view&message=server_error");
        }
    }

}
