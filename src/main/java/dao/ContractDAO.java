package dao;

import connectDB.ConnectDatabase;
import java.sql.*;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ContractDAO {

    public void createContract(int trainerId, int customerId, int packageId, LocalDate startDate, LocalDate endDate) {
        String sql = "INSERT INTO Contracts (TrainerID, CustomerID, PackageID, StartDate, EndDate, Status, ContractType) " +
                     "VALUES (?, ?, ?, ?, ?, 'active', 'package')";
        ConnectDatabase db = ConnectDatabase.getInstance();
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = db.openConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, trainerId);
            ps.setInt(2, customerId);
            ps.setInt(3, packageId);
            ps.setDate(4, Date.valueOf(startDate));
            ps.setDate(5, Date.valueOf(endDate));
            ps.executeUpdate();
        } catch (Exception e) {
            Logger.getLogger(ContractDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException ex) {
                Logger.getLogger(ContractDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public boolean isPackageActiveForCustomer(int customerId, int packageId) {
        String sql = "SELECT * FROM Contracts "
                + "WHERE CustomerID = ? AND PackageID = ? AND EndDate >= ? AND Status = 'active'";
        ConnectDatabase db = ConnectDatabase.getInstance();
        try (Connection con = db.openConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            ps.setInt(2, packageId);
            ps.setDate(3, Date.valueOf(LocalDate.now()));
            ResultSet rs = ps.executeQuery();
            return rs.next(); 
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
