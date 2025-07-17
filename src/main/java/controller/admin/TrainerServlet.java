
package controller.admin;

import dao.TrainerDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import model.Trainer;

@WebServlet(name="TrainerServlet", urlPatterns={"/TrainerServlet"})
public class TrainerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        TrainerDAO trainerDAO = new TrainerDAO();
        ArrayList<Trainer> trainerList = trainerDAO.getAllTrainers();
        request.setAttribute("trainerList", trainerList);
        request.getRequestDispatcher("/adminDashboard/viewtrainer.jsp").forward(request, response);
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