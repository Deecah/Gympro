

package controller;

import dao.*;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.Feedback;
import model.Program;
import model.Trainer;
import model.Package;

@WebServlet(name="PackageDetailServlet", urlPatterns={"/PackageDetailServlet"})
public class PackageDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int packageId = Integer.parseInt(request.getParameter("packageId"));

        // DAO lấy dữ liệu
        PackageDAO packageDAO = new PackageDAO();
        TrainerDAO trainerDAO = new TrainerDAO();
        ProgramDAO programDAO = new ProgramDAO();
        FeedbackDAO feedbackDAO = new FeedbackDAO();

        // 1. Gói tập
        Package p = packageDAO.getPackageById(packageId);

        // 2. Huấn luyện viên của gói
        Trainer trainer = trainerDAO.getProfile(p.getTrainerID());

        // 3. Danh sách chương trình của gói
//        List<Program> programs = programDAO.getProgramsByPackageId(packageId);

        // 4. Danh sách feedback của gói
        List<Feedback> feedbacks = feedbackDAO.getFeedbacksByPackageId(packageId);

        // Set dữ liệu
        request.setAttribute("pkg", p);
        request.setAttribute("trainer", trainer);
//        request.setAttribute("programs", programs);
        request.setAttribute("feedbacks", feedbacks);

        // Forward sang JSP
        request.getRequestDispatcher("package-detail.jsp").forward(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

    }
}
