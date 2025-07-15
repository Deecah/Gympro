
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

@WebServlet(name="UserServlet", urlPatterns={"/UserServlet"})
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
            case "view":
            {
                UserDAO userDAO = new UserDAO();
        ArrayList<User> userList = userDAO.getAllUsers();
        request.setAttribute("userList", userList);
        request.getRequestDispatcher("/adminDashboard/viewuser.jsp").forward(request, response);
        break;
            }
            default:
    {
                UserDAO userDAO = new UserDAO();
        ArrayList<User> userList = userDAO.getAllUsers();
        request.setAttribute("userList", userList);
        request.getRequestDispatcher("/adminDashboard/viewuser.jsp").forward(request, response);
        break;
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
  
    }

       private void handleBanUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            int userId = Integer.parseInt(request.getParameter("userId"));
            UserDAO userDAO = new UserDAO();
            boolean success = userDAO.banUser(userId); // Giả sử có phương thức banUser trong DAO
            if (success) {
                out.print("Đã cấm người dùng ID: " + userId + " thành công.");
            } else {
                out.print("Không thể cấm người dùng ID: " + userId + ". Có lỗi xảy ra.");
            }
        } catch (NumberFormatException e) {
            out.print("ID người dùng không hợp lệ.");
        } catch (Exception e) {
            e.printStackTrace();
            out.print("Lỗi server khi cấm người dùng: " + e.getMessage());
        }
    }

    private void handleUnbanUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            int userId = Integer.parseInt(request.getParameter("userId"));
            UserDAO userDAO = new UserDAO();
            boolean success = userDAO.unbanUser(userId); // Giả sử có phương thức unbanUser trong DAO
            if (success) {
                out.print("Đã bỏ cấm người dùng ID: " + userId + " thành công.");
            } else {
                out.print("Không thể bỏ cấm người dùng ID: " + userId + ". Có lỗi xảy ra.");
            }
        } catch (NumberFormatException e) {
            out.print("ID người dùng không hợp lệ.");
        } catch (Exception e) {
            e.printStackTrace();
            out.print("Lỗi server khi bỏ cấm người dùng: " + e.getMessage());
        }
    }

}