package controller;

import Utils.CloudinaryUploader;
import dao.PackageDAO;
import model.Package;
import model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

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
            String imageUrl;

            Part filePart = request.getPart("imageFile");
            if (filePart == null || filePart.getSize() == 0 || filePart.getSubmittedFileName() == null || filePart.getSubmittedFileName().isEmpty()) {
                request.setAttribute("error", "Vui lòng chọn ảnh để tải lên.");
                request.getRequestDispatcher("trainer/create-package.jsp").forward(request, response);
                return;
            }

            try {
                String folder = "packages/trainer_" + trainerId;
                imageUrl = CloudinaryUploader.upload(filePart.getInputStream(), filePart.getContentType(), folder);
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("error", "Lỗi khi upload ảnh lên Cloudinary.");
                request.getRequestDispatcher("trainer/create-package.jsp").forward(request, response);
                return;
            }

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

            int newId = packageDAO.insertPackage(p, trainerId);
            if (newId > 0) {
                response.sendRedirect("TrainerPackageServlet?action=list");
            } else {
                request.setAttribute("error", "Không tạo được gói tập.");
                request.getRequestDispatcher("trainer/create-package.jsp").forward(request, response);
            }
        } else if ("update".equals(action)) {
            int id = Integer.parseInt(request.getParameter("packageId"));
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            double price = Double.parseDouble(request.getParameter("price"));
            int duration = Integer.parseInt(request.getParameter("duration"));
            String currentImageUrl = request.getParameter("currentImageUrl");

            String imageUrl = currentImageUrl; // mặc định giữ nguyên ảnh cũ

            Part filePart = request.getPart("imageFile");

            // ✅ Kiểm tra kỹ file ảnh có hợp lệ không
            boolean isValidImage = filePart != null
                    && filePart.getSize() > 0
                    && filePart.getSubmittedFileName() != null
                    && !filePart.getSubmittedFileName().isEmpty()
                    && filePart.getContentType() != null
                    && filePart.getContentType().startsWith("image/");

            if (isValidImage) {
                try (InputStream inputStream = filePart.getInputStream()) {
                    String folder = "packages/trainer_" + trainerId;
                    imageUrl = CloudinaryUploader.upload(inputStream, filePart.getContentType(), folder);
                    System.out.println("✅ Upload ảnh mới thành công: " + imageUrl);
                } catch (Exception e) {
                    e.printStackTrace();
                    request.setAttribute("error", "Upload ảnh thất bại.");
                    request.setAttribute("package", packageDAO.getPackageByIdAndTrainerId(id, trainerId));
                    request.getRequestDispatcher("trainer/edit-package.jsp").forward(request, response);
                    return;
                }
            } else {
                System.out.println("⚠️ Không có ảnh mới hoặc ảnh không hợp lệ. Giữ ảnh cũ: " + currentImageUrl);
            }

            // cập nhật thông tin gói tập
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
                request.setAttribute("error", "Cập nhật thất bại.");
                request.setAttribute("package", p);
                request.getRequestDispatcher("trainer/edit-package.jsp").forward(request, response);
            }
        }

    }
}
