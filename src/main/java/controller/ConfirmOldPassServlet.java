package controller;

import dao.UserDAO;
import model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "ConfirmOldPassServlet", urlPatterns = {"/ConfirmOldPassServlet"})
public class ConfirmOldPassServlet extends HttpServlet {
} 