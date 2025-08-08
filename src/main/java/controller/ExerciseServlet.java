package controller;

import dao.ExerciseDAO;
import model.Exercise;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/ExerciseServlet")
public class ExerciseServlet extends HttpServlet {
    private ExerciseDAO dao = new ExerciseDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}