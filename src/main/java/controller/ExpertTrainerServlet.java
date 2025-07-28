package controller;

import dao.TrainerDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import model.Trainer;

@WebServlet(name = "ExpertTrainerServlet", urlPatterns = {"/ExpertTrainerServlet"})
public class ExpertTrainerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        TrainerDAO trainerDAO = new TrainerDAO();

        // Lấy từ khóa tìm kiếm (nếu có)
        String keyword = request.getParameter("keyword");
        List<Trainer> trainers; // ✅ Chỉ khai báo 1 lần

        // Nếu có từ khóa thì tìm theo keyword, không thì lấy toàn bộ
        if (keyword != null && !keyword.trim().isEmpty()) {
            trainers = trainerDAO.searchByKeyword(keyword.trim());
        } else {
            trainers = trainerDAO.getAllTrainers();
        }

        if (trainers == null) {
            trainers = new ArrayList<>();
        }

        // Phân trang
        int page = 1;
        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            try {
                page = Integer.parseInt(pageParam);
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        int pageSize = 8;
        int total = trainers.size();
        int totalPages = (int) Math.ceil((double) total / pageSize);
        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, total);
        List<Trainer> paginatedList = trainers.subList(start, end);

        // Truyền dữ liệu cho JSP
        request.setAttribute("trainers", paginatedList);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("keyword", keyword); // để hiển thị lại keyword trên ô input

        request.getRequestDispatcher("./trainers.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response); // xử lý POST giống GET
    }
}


