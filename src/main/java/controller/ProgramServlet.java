package controller;

import dao.ExerciseLibraryDAO;
import dao.PackageDAO;
import dao.ProgramDAO;
import dao.UserDAO;
import model.ExerciseLibrary;
import model.Package;
import model.Program;
import model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/ProgramServlet")
public class ProgramServlet extends HttpServlet {
    private ProgramDAO programDAO = new ProgramDAO();
    private UserDAO userDAO = new UserDAO();
    private ExerciseLibraryDAO exerciseLibraryDAO = new ExerciseLibraryDAO();
    private PackageDAO packageDAO = new PackageDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String userId = null;
        String role = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equalsIgnoreCase("userId") && cookie.getValue() != null) {
                userId = cookie.getValue();
            } else if (cookie.getName().equalsIgnoreCase("role") && cookie.getValue() != null) {
                role = cookie.getValue();
            }
        }
        if (userId == null || role == null || !role.equalsIgnoreCase("Trainer")) {
            response.sendRedirect("login.jsp");
            return;
        }

        List<Package> packages = packageDAO.getAllPackagesByTrainer(Integer.parseInt(userId));
        List<Program> programs = programDAO.getAllPrograms(Integer.parseInt(userId));
        List<User> customers = userDAO.getCustomersByTrainer(Integer.parseInt(userId));
        List<ExerciseLibrary> exerciseLibraries = exerciseLibraryDAO.getAllExercises(Integer.parseInt(userId));
        request.setAttribute("programs", programs);
        request.setAttribute("customers", customers);
        request.setAttribute("exerciseLibraries", exerciseLibraries);
        request.setAttribute("programId", request.getParameter("programId"));
        request.setAttribute("packages",packages);
        request.getRequestDispatcher("/trainer/programs.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String userId = null;
        String role = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equalsIgnoreCase("userId") && cookie.getValue() != null) {
                userId = cookie.getValue();
            } else if (cookie.getName().equalsIgnoreCase("role") && cookie.getValue() != null) {
                role = cookie.getValue();
            }
        }
        if (userId == null || role == null || !role.equalsIgnoreCase("Trainer")) {
            response.sendRedirect("login.jsp");
            return;
        }

        String action = request.getParameter("action");
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        int packageId = Integer.parseInt(request.getParameter("packageId"));

        if ("create".equals(action)) {
            if (packageId == 0) {
                response.sendRedirect("ProgramServlet?error=Package ID is required");
                return;
            }
            Program program = new Program();
            program.setName(name);
            program.setDescription(description);
            program.setTrainerId(Integer.parseInt(userId));
            program.setPackageId(packageId);
            boolean success = programDAO.createProgram(program);
            if (success) {
                response.sendRedirect("ProgramServlet?success=Program created successfully");
            } else {
                response.sendRedirect("ProgramServlet?error=Failed to create program");
            }
        } else if ("edit".equals(action)) {
            if (packageId == 0) {
                response.sendRedirect("ProgramServlet?programId=" + request.getParameter("programId") + "&error=Package ID is required");
                return;
            }
            int programId = Integer.parseInt(request.getParameter("programId"));
            Program program = new Program();
            program.setProgramId(programId);
            program.setName(name);
            program.setDescription(description);
            program.setTrainerId(Integer.parseInt(userId));
            program.setPackageId(packageId);
            boolean success = programDAO.updateProgram(program);
            if (success) {
                response.sendRedirect("ProgramServlet?programId=" + programId + "&success=Program updated successfully");
            } else {
                response.sendRedirect("ProgramServlet?programId=" + programId + "&error=Failed to update program");
            }
        }
    }
}