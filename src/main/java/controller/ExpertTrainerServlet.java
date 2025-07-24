package controller;

import dao.TrainerDAO;
import java.io.IOException;
import java.io.PrintWriter;
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
        ArrayList<Trainer> trainers = trainerDAO.getAllTrainers();

        if (trainers == null) {
            trainers = new ArrayList<>();
        }

        // Lấy trang hiện tại từ param (mặc định là 1)
        int page = 1;
        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            try {
                page = Integer.parseInt(pageParam);
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        // Cấu hình phân trang
        int pageSize = 8;
        int total = trainers.size();
        int totalPages = (int) Math.ceil((double) total / pageSize);

        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, total);
        List<Trainer> paginatedList = trainers.subList(start, end);

        // Gửi dữ liệu lên JSP
        request.setAttribute("trainers", paginatedList);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);

        request.getRequestDispatcher("trainers.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
