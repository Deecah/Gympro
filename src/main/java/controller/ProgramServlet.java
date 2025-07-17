package controller;

import dao.PackageDAO;
import dao.ProgramDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

import model.Package;
import model.Program;
import model.User;

@WebServlet(name = "ProgramServlet", urlPatterns = {"/ProgramServlet"})
public class ProgramServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        User user = (User) session.getAttribute("user");
        if (!"Trainer".equalsIgnoreCase(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        ProgramDAO dao = new ProgramDAO();
        List<Program> programs = dao.getAllProgramsByTrainer(user.getUserId());
        List<Package> packageList = new PackageDAO().getAllPackagesByTrainer(user.getUserId());

        request.setAttribute("programs", programs);
        request.setAttribute("packageList", packageList);

        request.getRequestDispatcher("trainer/programs.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        User user = (User) session.getAttribute("user");
        if (!"Trainer".equalsIgnoreCase(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String packageIdStr = request.getParameter("packageId");

        Program program = new Program();
        program.setName(name);
        program.setDescription(description);

        try {
            int packageId = Integer.parseInt(packageIdStr);
            program.setPackageId(packageId);
        } catch (NumberFormatException e) {
            program.setPackageId(0); 
        }

        ProgramDAO dao = new ProgramDAO();
        dao.addProgram(program, user.getUserId());

        response.sendRedirect(request.getContextPath() + "/ProgramServlet");
    }
}
