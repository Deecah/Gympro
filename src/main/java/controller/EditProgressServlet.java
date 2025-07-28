package controller;

import dao.ProgressDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Progress;

import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet("/editProgress")
public class EditProgressServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idStr = request.getParameter("id");
        if (idStr != null && !idStr.isEmpty()) {
            try {
                int id = Integer.parseInt(idStr);
                Progress progress = ProgressDAO.getProgressById(id);
                if (progress != null) {
                    request.setAttribute("progress", progress);
                    request.getRequestDispatcher("editProgress.jsp").forward(request, response);
                    return;
                }
            } catch (NumberFormatException e) {
                e.printStackTrace(); // log lỗi parse
            }
        }

        // Nếu không có id hoặc lỗi thì quay về trang progress
        response.sendRedirect("progress");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String recordedAtStr = request.getParameter("recordedAt");
            double weight = Double.parseDouble(request.getParameter("weight"));
            double bodyFat = Double.parseDouble(request.getParameter("bodyFatPercent"));
            double muscleMass = Double.parseDouble(request.getParameter("muscleMass"));
            String notes = request.getParameter("notes");

            Progress p = ProgressDAO.getProgressById(id);
            if (p != null) {
                p.setRecordedAt(LocalDateTime.parse(recordedAtStr));
                p.setWeight(weight);
                p.setBodyFatPercent(bodyFat);
                p.setMuscleMass(muscleMass);
                p.setNotes(notes);

                ProgressDAO.updateProgress(p);
            }

            response.sendRedirect("progress");

        } catch (Exception e) {
            e.printStackTrace();
            // Có lỗi thì forward lại trang chỉnh sửa với thông báo lỗi
            request.setAttribute("error", "Failed to update progress. Please check your input.");
            request.getRequestDispatcher("editProgress.jsp").forward(request, response);
        }
    }
}
