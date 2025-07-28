package controller;

import dao.ProgramDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;

@WebServlet(name="DeleteProgramServlet", urlPatterns={"/DeleteProgramServlet"})
public class DeleteProgramServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null || !"Trainer".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        
        try {
            int programId = Integer.parseInt(request.getParameter("programId"));
            int trainerId = user.getUserId();
            
            ProgramDAO programDAO = new ProgramDAO();
            
            // Kiểm tra program có thuộc về trainer này không
            if (programDAO.getProgramById(programId, trainerId) == null) {
                response.sendRedirect(request.getContextPath() + "/ProgramServlet?error=unauthorized");
                return;
            }
            
            // Xóa program
            if (programDAO.deleteProgram(programId)) {
                response.sendRedirect(request.getContextPath() + "/ProgramServlet?success=deleted");
            } else {
                response.sendRedirect(request.getContextPath() + "/ProgramServlet?error=delete_failed");
            }
            
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/ProgramServlet?error=server_error");
        }
    }
} 