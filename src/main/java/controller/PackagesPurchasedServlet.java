package controller;

import connectDB.ConnectDatabase;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;

@WebServlet(name = "PackagesPurchasedServlet", urlPatterns = {"/packagesPurchased"})
public class PackagesPurchasedServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        HttpSession session = request.getSession();
        model.User user = (model.User) session.getAttribute("user");

        if (user == null || !"Customer".equals(user.getRole())) {
            response.sendRedirect("login.jsp");
            return;
        }

        List<Object[]> purchasedList = new ArrayList<>();
        String sql = "SELECT p.PackageName, u.Name AS TrainerName, c.StartDate, c.EndDate, c.Status, p.PackageID " +
                     "FROM Contracts c " +
                     "JOIN Package p ON c.PackageID = p.PackageID " +
                     "JOIN Users u ON p.TrainerID = u.Id " +
                     "WHERE c.CustomerID = ?";

        try (Connection conn = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, user.getUserId());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Object[] row = new Object[6];
                row[0] = rs.getString("PackageName");
                row[1] = rs.getString("TrainerName");
                row[2] = rs.getDate("StartDate");
                row[3] = rs.getDate("EndDate");
                row[4] = rs.getString("Status");
                row[5] = rs.getInt("PackageID");  // for feedback if needed
                purchasedList.add(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        request.setAttribute("purchasedList", purchasedList);
        request.getRequestDispatcher("packagesPurchased.jsp").forward(request, response);
    }
}
