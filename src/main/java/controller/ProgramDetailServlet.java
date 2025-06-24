package controller;

import dao.ProgramDAO;
import dao.ProgramWeekDAO;
import dao.ProgramDayDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.RequestDispatcher;

import model.Program;
import model.ProgramWeek;
import model.ProgramDay;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.logging.Logger;
import java.util.logging.Level;

@WebServlet(name = "ProgramDetailServlet", urlPatterns = {"/ProgramDetailServlet"})
public class ProgramDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String programIdStr = request.getParameter("programId");
            int programId = Integer.parseInt(programIdStr);

            ProgramDAO programDAO = new ProgramDAO();
            ProgramWeekDAO weekDAO = new ProgramWeekDAO();
            ProgramDayDAO dayDAO = new ProgramDayDAO();

            Program program = programDAO.getProgramById(programId, 1);
            List<ProgramWeek> weeks = weekDAO.getWeeksByProgramId(programId);

            Map<Integer, List<ProgramDay>> daysMap = new HashMap<>();
            for (ProgramWeek w : weeks) {
                List<ProgramDay> days = dayDAO.getDaysByWeekId(w.getWeekId());
                daysMap.put(w.getWeekId(), days);
            }

            request.setAttribute("program", program);
            request.setAttribute("weeks", weeks);
            request.setAttribute("daysMap", daysMap);

            RequestDispatcher rd = request.getRequestDispatcher("/trainer/program_detail.jsp");
            rd.forward(request, response);

        } catch (Exception e) {
            Logger.getLogger(ProgramDetailServlet.class.getName()).log(Level.SEVERE, "Error loading program detail", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Something went wrong");
        }
    }
}
