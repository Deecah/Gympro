
package controller;

import dao.ContractDAO;
import dao.ProgressDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.*;

import model.User;
import model.CustomerProgressDTO;

@WebServlet(name = "CustomerServlet", urlPatterns = {"/CustomerServlet"})
public class CustomerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String userId = null;
        String role = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equalsIgnoreCase("userId") && cookie.getValue() != null) {
                userId = cookie.getValue();
            } else if (cookie.getName().equalsIgnoreCase("role") && cookie.getValue() != null) {
                role = cookie.getValue();
            }
        }
        if (userId == null || role == null || !role.equalsIgnoreCase("Trainer")) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            // Hiển thị danh sách khách hàng với tổng quan tiến độ
            showCustomersList(request, response, Integer.parseInt(userId));

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(500, "Internal server error");
        }
    }

    private void showCustomersList(HttpServletRequest request, HttpServletResponse response, int trainerId)
            throws ServletException, IOException {

        // Lấy danh sách khách hàng có hợp đồng với trainer
        ContractDAO contractDAO = new ContractDAO();
        ArrayList<User> customers = contractDAO.getCustomersByTrainer(trainerId);

        // Lấy thông tin tiến độ cho từng khách hàng
        ProgressDAO progressDAO = new ProgressDAO();
        ArrayList<CustomerProgressDTO> customerProgressList = new ArrayList<>();

        for (User customer : customers) {
            // Lấy thống kê tiến độ theo từng chương trình cho khách hàng này
            List<CustomerProgressDTO> programStats = progressDAO.getProgramProgressStats(customer.getUserId());

            for (CustomerProgressDTO programProgress : programStats) {
                // Set thông tin tên khách hàng cho mỗi tiến độ chương trình
                programProgress.setCustomerName(customer.getUserName());
                customerProgressList.add(programProgress);
            }
        }

        request.setAttribute("customers", customerProgressList);
        request.getRequestDispatcher("trainer/customer.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
