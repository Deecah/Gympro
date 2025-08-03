package controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import com.google.gson.Gson;
import java.util.ArrayList;
import dao.ProgramWeekDAO;

@WebServlet(name = "ProgramWeekServlet", urlPatterns = {"/ProgramWeekServlet"})
public class ProgramWeekServlet extends HttpServlet {
  
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String action = request.getParameter("action");

        try {
            if ("add".equalsIgnoreCase(action)) {
                int programId = Integer.parseInt(request.getParameter("programId"));
                int weekNumber = Integer.parseInt(request.getParameter("weekNumber"));

                int weekId = ProgramWeekDAO.addProgramWeek(programId, weekNumber);
                ArrayList<Integer> dayIds = ProgramWeekDAO.addDaysForWeek(weekId);

                response.setContentType("application/json");
                response.getWriter().write(new Gson().toJson(new AddWeekResponse(weekId, dayIds)));

            } else if ("delete".equalsIgnoreCase(action)) {
                int weekId = Integer.parseInt(request.getParameter("weekId"));
                boolean deleted = ProgramWeekDAO.deleteProgramWeek(weekId);

                response.setContentType("application/json");
                response.getWriter().write("{\"deleted\": " + deleted + "}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    private static class AddWeekResponse {

        int weekId;
        ArrayList<Integer> dayIds;

        public AddWeekResponse(int weekId, ArrayList<Integer> dayIds) {
            this.weekId = weekId;
            this.dayIds = dayIds;
        }
    }


}
