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
    
    /**
     * Lấy danh sách khách hàng có contract với trainer
     */
    public java.util.ArrayList<model.User> getCustomersByTrainer(int trainerId) {
        java.util.ArrayList<model.User> customerList = new java.util.ArrayList<>();
        String sql = "SELECT DISTINCT u.Id, u.Name, u.Gender, u.Email, u.Phone, u.Address, u.AvatarUrl, u.Role, u.Status " +
                     "FROM Users u " +
                     "INNER JOIN Contracts c ON u.Id = c.CustomerID " +
                     "WHERE c.TrainerID = ? AND c.Status = 'active' AND c.EndDate >= ? AND u.Role = 'Customer' " +
                     "ORDER BY u.Name";
        
        System.out.println("=== DEBUG ContractDAO.getCustomersByTrainer ===");
        System.out.println("Trainer ID: " + trainerId);
        System.out.println("Current Date: " + LocalDate.now());
        System.out.println("SQL Query: " + sql);
        
        try (Connection con = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, trainerId);
            ps.setDate(2, Date.valueOf(LocalDate.now()));
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                model.User user = new model.User();
                user.setUserId(rs.getInt("Id"));
                user.setUserName(rs.getString("Name"));
                user.setGender(rs.getString("Gender"));
                user.setEmail(rs.getString("Email"));
                user.setPhone(rs.getString("Phone"));
                user.setAddress(rs.getString("Address"));
                user.setAvatarUrl(rs.getString("AvatarUrl"));
                user.setRole(rs.getString("Role"));
                user.setStatus(rs.getString("Status"));
                customerList.add(user);
                System.out.println("Found customer: " + user.getUserName() + " (ID: " + user.getUserId() + ")");
            }
            
        } catch (Exception e) {
            Logger.getLogger(ContractDAO.class.getName()).log(Level.SEVERE, "Get customers by trainer failed", e);
            System.out.println("Error in getCustomersByTrainer: " + e.getMessage());
        }
        
        System.out.println("Total customers found: " + customerList.size());
        System.out.println("=== END DEBUG ContractDAO ===");
        return customerList;
    }
    
    /**
     * Debug method để kiểm tra tất cả contracts
     */
    public void debugAllContracts() {
        String sql = "SELECT * FROM Contracts ORDER BY Id";
        System.out.println("=== DEBUG ALL CONTRACTS ===");
        
        try (Connection con = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                int id = rs.getInt("Id");
                int trainerId = rs.getInt("TrainerID");
                int customerId = rs.getInt("CustomerID");
                String status = rs.getString("Status");
                java.sql.Date endDate = rs.getDate("EndDate");
                java.sql.Date startDate = rs.getDate("StartDate");
                
                System.out.println("Contract ID: " + id + 
                                 ", TrainerID: " + trainerId + 
                                 ", CustomerID: " + customerId + 
                                 ", Status: " + status + 
                                 ", StartDate: " + startDate + 
                                 ", EndDate: " + endDate);
            }
            
        } catch (Exception e) {
            System.out.println("Error in debugAllContracts: " + e.getMessage());
        }
        System.out.println("=== END DEBUG ALL CONTRACTS ===");
    }
}
