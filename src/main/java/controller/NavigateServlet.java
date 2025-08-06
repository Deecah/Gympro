/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import java.io.IOException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;

@WebServlet(name="NavigateServlet", urlPatterns={"/Navigate"})
public class NavigateServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        User user = (User) session.getAttribute("user");
        String role = user.getRole(); 
        String target = request.getParameter("target");

        String redirectURL = "";

        switch (target) {
            case "backtohome":
                redirectURL = role.equalsIgnoreCase("trainer") ? "trainer/trainer.jsp" : "index.jsp";
                break;
            case "settings":
                redirectURL = role.equalsIgnoreCase("trainer") ? "trainer/settings.jsp" : "settings.jsp";
                break;
            case "profile":
                redirectURL = role.equalsIgnoreCase("trainer") ? "trainer/profile-trainer.jsp" : "profile.jsp";
                break;
        }

        response.sendRedirect(redirectURL);
    }
}
