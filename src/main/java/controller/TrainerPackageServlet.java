package controller;

import dao.PackageDAO;
import model.Package;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.*;
import java.nio.file.Paths;
import java.util.List;
import model.User;

@MultipartConfig
@WebServlet(name = "TrainerPackageServlet", urlPatterns = {"/TrainerPackageServlet"})
public class TrainerPackageServlet extends HttpServlet {

    private final PackageDAO packageDAO = new PackageDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null || !"Trainer".equalsIgnoreCase(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        int trainerId = user.getUserId();

        if (action == null || action.equals("list")) {
            List<Package> packages = packageDAO.getAllPackagesByTrainer(trainerId);
            request.setAttribute("packages", packages);
            request.getRequestDispatcher("trainer/package.jsp").forward(request, response);

        } else if (action.equals("create")) {
            request.getRequestDispatcher("trainer/create-package.jsp").forward(request, response);

        } else if (action.equals("edit")) {
            int packageId = Integer.parseInt(request.getParameter("id"));
            Package p = packageDAO.getPackageByIdAndTrainerId(packageId, trainerId);
            if (p != null) {
                request.setAttribute("package", p);
                request.getRequestDispatcher("trainer/edit-package.jsp").forward(request, response);
            } else {
                response.sendRedirect("TrainerPackageServlet?action=list");
            }

        } else if (action.equals("delete")) {
            int packageId = Integer.parseInt(request.getParameter("id"));
            packageDAO.deletePackage(packageId, trainerId);
            response.sendRedirect("TrainerPackageServlet?action=list");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null || !"Trainer".equalsIgnoreCase(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        int trainerId = user.getUserId();

        if ("create".equals(action)) {
            Part filePart = request.getPart("imageFile");
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            String uploadPath = getServletContext().getRealPath("/upload");
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            filePart.write(uploadPath + File.separator + fileName);
            String imageUrl = request.getContextPath() + "/upload/" + fileName;

            String name = request.getParameter("name");
            String description = request.getParameter("description");
            double price = Double.parseDouble(request.getParameter("price"));
            int duration = Integer.parseInt(request.getParameter("duration"));

            Package p = new Package();
            p.setName(name);
            p.setDescription(description);
            p.setImageUrl(imageUrl);
            p.setPrice(price);
            p.setDuration(duration);

            packageDAO.insertPackage(p, trainerId);
            response.sendRedirect("TrainerPackageServlet?action=list");

        } else if ("update".equals(action)) {
            int id = Integer.parseInt(request.getParameter("packageId"));
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            double price = Double.parseDouble(request.getParameter("price"));
            int duration = Integer.parseInt(request.getParameter("duration"));

            String currentImageUrl = request.getParameter("currentImageUrl");

            Part filePart = request.getPart("imageFile");
            String imageUrl = currentImageUrl;

            if (filePart != null && filePart.getSize() > 0 && filePart.getSubmittedFileName() != null && !filePart.getSubmittedFileName().isEmpty()) {
                String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                String uploadPath = getServletContext().getRealPath("/upload");
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }
                filePart.write(uploadPath + File.separator + fileName);

                imageUrl = request.getContextPath() + "/upload/" + fileName;
            }

            Package p = new Package();
            p.setPackageID(id);
            p.setName(name);
            p.setDescription(description);
            p.setImageUrl(imageUrl);
            p.setPrice(price);
            p.setDuration(duration);

            boolean updated = packageDAO.updatePackage(p, trainerId);
            if (updated) {
                response.sendRedirect("TrainerPackageServlet?action=list");
            } else {
                request.setAttribute("error", "Update failed.");
                request.setAttribute("package", p);
                request.getRequestDispatcher("trainer/edit-package.jsp").forward(request, response);
            }
        }

    }
}
