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
        if (idStr != null) {
            int id = Integer.parseInt(idStr);
            Progress progress = ProgressDAO.getProgressById(id);
            request.setAttribute("progress", progress);
            request.getRequestDispatcher("editProgress.jsp").forward(request, response);
        } else {
            response.sendRedirect("progress");
        }
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
            p.setRecordedAt(LocalDateTime.parse(recordedAtStr));
            p.setWeight(weight);
            p.setBodyFatPercent(bodyFat);
            p.setMuscleMass(muscleMass);
            p.setNotes(notes);

            ProgressDAO.updateProgress(p);

        } catch (Exception e) {
            e.printStackTrace();
        }

        // ✅ Redirect về lại danh sách progress sau khi update
        response.sendRedirect("progress");
    }
}
