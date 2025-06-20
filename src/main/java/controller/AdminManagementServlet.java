package controller;


import dao.TrainerDAO;
import dao.UserDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Trainer;
import model.User;

@WebServlet(name = "AdminManagementServlet", urlPatterns = {"/AdminManagementServlet"})
public class AdminManagementServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) {
            action = "initial_load"; // Mặc định hiển thị user list hoặc dashboard
        }

        switch (action) {
            case "viewuser":
                handleViewUsers(request, response);
                break;
            case "viewtrainer":
                handleViewTrainers(request, response);
                break;
            case "viewreport":
                handleViewReports(request, response);
                break;
            case "banuser":
                handleBanUser(request, response);
                break;
            case "unbanuser":
                handleUnbanUser(request, response);
                break;
            // Thêm các case khác cho các hành động quản lý nội dung nếu cần AJAX
            default:
                // Nếu không phải AJAX request cho một phần cụ thể, có thể forward
                // đến trang chính hoặc hiển thị một thông báo
                request.getRequestDispatcher("adminDashboard.jsp").forward(request, response);
                break;
        }
    }

    private void handleViewUsers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            // Lấy danh sách người dùng từ DAO của bạn
            UserDAO userDAO = new UserDAO(); // Khởi tạo DAO
            List<User> userList = userDAO.getAllUsers(); // Giả sử có phương thức này

            // Bắt đầu xây dựng chuỗi HTML của bảng
            StringBuilder htmlTable = new StringBuilder();
            htmlTable.append("<table class=\"table table-striped table-hover\">");
            htmlTable.append("<thead>");
            htmlTable.append("<tr>");
            htmlTable.append("<th>ID</th>");
            htmlTable.append("<th>Name</th>");
            htmlTable.append("<th>Email</th>");
            htmlTable.append("<th>Role</th>");
            htmlTable.append("<th>Status</th>");
            htmlTable.append("<th class=\"actions-cell\">Actions</th>");
            htmlTable.append("</tr>");
            htmlTable.append("</thead>");
            htmlTable.append("<tbody>");

            if (userList != null && !userList.isEmpty()) {
                for (User u : userList) {
                    htmlTable.append("<tr>");
                    htmlTable.append("<td>").append(u.getUserId()).append("</td>");
                    htmlTable.append("<td>").append(u.getUserName()).append("</td>");
                    htmlTable.append("<td>").append(u.getEmail()).append("</td>");
                    htmlTable.append("<td>").append(u.getRole()).append("</td>");
                    htmlTable.append("<td>").append(u.getStatus()).append("</td>");
                    htmlTable.append("<td class=\"actions-cell\">");
                    htmlTable.append("<div class=\"dropdown-container\">");
                    htmlTable.append("<button class=\"btn btn-sm dropdown-toggle\" type=\"button\" id=\"dropdownMenuButton").append(u.getUserId()).append("\" data-bs-toggle=\"dropdown\" aria-expanded=\"false\" style=\"border: none; background: none; padding: 0;\">");
                    htmlTable.append("<i class=\"fas fa-chevron-down dropdown-arrow\"></i>");
                    htmlTable.append("</button>");
                    htmlTable.append("<ul class=\"dropdown-menu\" aria-labelledby=\"dropdownMenuButton").append(u.getUserId()).append("\">");
                    htmlTable.append("<li><a class=\"dropdown-item\" href=\"#\" onclick=\"viewUserProfile(").append(u.getUserId()).append(")\">View Profile</a></li>");
                    if ("normal".equalsIgnoreCase(u.getStatus())) {
                        htmlTable.append("<li><a class=\"dropdown-item text-danger\" href=\"#\" onclick=\"banUser(").append(u.getUserId()).append(")\">Ban User</a></li>");
                    } else if ("banned".equalsIgnoreCase(u.getStatus())) {
                        htmlTable.append("<li><a class=\"dropdown-item text-success\" href=\"#\" onclick=\"unbanUser(").append(u.getUserId()).append(")\">Unban User</a></li>");
                    }
                    htmlTable.append("</ul>");
                    htmlTable.append("</div>");
                    htmlTable.append("</td>");
                    htmlTable.append("</tr>");
                }
            } else {
                htmlTable.append("<tr><td colspan=\"6\" class=\"text-center text-muted\">Không có người dùng nào để hiển thị.</td></tr>");
            }
            htmlTable.append("</tbody>");
            htmlTable.append("</table>");

            out.print(htmlTable.toString()); // Gửi chuỗi HTML về client
        } catch (Exception e) {
            e.printStackTrace();
            out.print("<p class=\"text-center text-danger\">Lỗi khi tải danh sách người dùng: " + e.getMessage() + "</p>");
        }
    }

    private void handleViewTrainers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            // Lấy danh sách trainer từ DAO của bạn
            TrainerDAO trainerDAO = new TrainerDAO(); // Khởi tạo DAO
            List<Trainer> trainerList = trainerDAO.getAllTrainers(); // Giả sử có phương thức này

            // Bắt đầu xây dựng chuỗi HTML của bảng
            StringBuilder htmlTable = new StringBuilder();
            htmlTable.append("<table class=\"table table-striped table-hover\">");
            htmlTable.append("<thead>");
            htmlTable.append("<tr>");
            htmlTable.append("<th>Avatar</th>");
            htmlTable.append("<th>ID</th>");
            htmlTable.append("<th>Name</th>");
            htmlTable.append("<th>Email</th>");
            htmlTable.append("<th>Specialization</th>");
            htmlTable.append("<th>Status</th>");
            htmlTable.append("<th class=\"actions-cell\">Actions</th>");
            htmlTable.append("</tr>");
            htmlTable.append("</thead>");
            htmlTable.append("<tbody>");

            if (trainerList != null && !trainerList.isEmpty()) {
                for (Trainer t : trainerList) {
                    htmlTable.append("<tr>");
                    htmlTable.append("<td><img src=\"").append(t.getAvatarUrl()).append("\" alt=\"").append(t.getUserName()).append(" Avatar\" width=\"50\" height=\"50\" style=\"object-fit: cover; border-radius: 50%;\"></td>");
                    htmlTable.append("<td>").append(t.getUserId()).append("</td>"); // Giả sử Trainer cũng có userId
                    htmlTable.append("<td>").append(t.getUserName()).append("</td>");
                    htmlTable.append("<td>").append(t.getEmail()).append("</td>");
                    htmlTable.append("<td>").append(t.getSpecialization()).append("</td>");
                    htmlTable.append("<td>").append(t.getStatus()).append("</td>");
                    htmlTable.append("<td class=\"actions-cell\">");
                    htmlTable.append("<div class=\"dropdown-container\">");
                    htmlTable.append("<button class=\"btn btn-sm dropdown-toggle\" type=\"button\" id=\"dropdownMenuButton").append(t.getUserId()).append("\" data-bs-toggle=\"dropdown\" aria-expanded=\"false\" style=\"border: none; background: none; padding: 0;\">");
                    htmlTable.append("<i class=\"fas fa-chevron-down dropdown-arrow\"></i>");
                    htmlTable.append("</button>");
                    htmlTable.append("<ul class=\"dropdown-menu\" aria-labelledby=\"dropdownMenuButton").append(t.getUserId()).append("\">");
                    htmlTable.append("<li><a class=\"dropdown-item\" href=\"#\" onclick=\"viewUserProfile(").append(t.getUserId()).append(")\">View Profile</a></li>");
                     if ("normal".equalsIgnoreCase(t.getStatus())) {
                        htmlTable.append("<li><a class=\"dropdown-item text-danger\" href=\"#\" onclick=\"banUser(").append(t.getUserId()).append(")\">Ban Trainer</a></li>");
                    } else if ("banned".equalsIgnoreCase(t.getStatus())) {
                        htmlTable.append("<li><a class=\"dropdown-item text-success\" href=\"#\" onclick=\"unbanUser(").append(t.getUserId()).append(")\">Unban Trainer</a></li>");
                    }
                    htmlTable.append("</ul>");
                    htmlTable.append("</div>");
                    htmlTable.append("</td>");
                    htmlTable.append("</tr>");
                }
            } else {
                htmlTable.append("<tr><td colspan=\"7\" class=\"text-center text-muted\">No data found !!.</td></tr>");
            }
            htmlTable.append("</tbody>");
            htmlTable.append("</table>");

            out.print(htmlTable.toString()); // Gửi chuỗi HTML về client
        } catch (Exception e) {
            e.printStackTrace();
            out.print("<p class=\"text-center text-danger\">Lỗi khi tải danh sách huấn luyện viên: " + e.getMessage() + "</p>");
        }
    }
    
    private void handleViewReports(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            // Đây là nơi bạn sẽ lấy dữ liệu báo cáo từ DAO
            // Giả lập dữ liệu báo cáo
            StringBuilder htmlTable = new StringBuilder();
            htmlTable.append("<table class=\"table table-striped table-hover\">");
            htmlTable.append("<thead>");
            htmlTable.append("<tr>");
            htmlTable.append("<th>Report ID</th>");
            htmlTable.append("<th>Reporter</th>");
            htmlTable.append("<th>Reported User</th>");
            htmlTable.append("<th>Content</th>");
            htmlTable.append("<th>Report Date</th>");
            htmlTable.append("<th>Status</th>");
            htmlTable.append("<th>Actions</th>");
            htmlTable.append("</tr>");
            htmlTable.append("</thead>");
            htmlTable.append("<tbody>");
            
            // Ví dụ dữ liệu tĩnh
            htmlTable.append("<tr>");
            htmlTable.append("<td>RPT001</td>");
            htmlTable.append("<td>UserX</td>");
            htmlTable.append("<td>TrainerY</td>");
            htmlTable.append("<td>Nội dung vi phạm</td>");
            htmlTable.append("<td>2024-06-19</td>");
            htmlTable.append("<td>Pending</td>");
            htmlTable.append("<td><button class='btn btn-sm btn-info'>Review</button></td>");
            htmlTable.append("</tr>");
            
            htmlTable.append("<tr><td colspan=\"7\" class=\"text-center text-muted\">Cần tích hợp backend để tải dữ liệu báo cáo thực tế.</td></tr>");

            htmlTable.append("</tbody>");
            htmlTable.append("</table>");
            
            out.print(htmlTable.toString());
        } catch (Exception e) {
            e.printStackTrace();
            out.print("<p class=\"text-center text-danger\">Lỗi khi tải báo cáo: " + e.getMessage() + "</p>");
        }
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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Admin Management Servlet handles admin actions via AJAX and direct requests";
    }
}