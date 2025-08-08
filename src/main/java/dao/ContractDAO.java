package dao;

import connectDB.ConnectDatabase;
import model.User;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;

public class ContractDAO {

    public int createContract(int trainerId, int customerId, int packageId, Date startDate, Date endDate) {
        // First validate that customer exists
        if (!customerExists(customerId)) {
            Logger.getLogger(ContractDAO.class.getName()).log(Level.SEVERE,
                    "Customer with ID " + customerId + " does not exist");
            return -1;
        }

        String sql = "INSERT INTO Contracts (TrainerID, CustomerID, PackageID, StartDate, EndDate, Status) " +
                "VALUES (?, ?, ?, ?, ?, 'active')";
        ConnectDatabase db = ConnectDatabase.getInstance();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = db.openConnection();
            ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, trainerId);
            ps.setInt(2, customerId);
            ps.setInt(3, packageId);
            ps.setDate(4, startDate);
            ps.setDate(5, Date.valueOf(endDate.toLocalDate()));
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (Exception e) {
            Logger.getLogger(ContractDAO.class.getName()).log(Level.SEVERE,
                    "Error creating contract: " + e.getMessage(), e);
        } finally {
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException ex) {
                Logger.getLogger(ContractDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return -1;
    }

    private boolean customerExists(int customerId) {
        String sql = "SELECT COUNT(*) FROM Customer WHERE Id = ?";
        ConnectDatabase db = ConnectDatabase.getInstance();
        try (Connection con = db.openConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            Logger.getLogger(ContractDAO.class.getName()).log(Level.SEVERE,
                    "Error checking customer existence: " + e.getMessage(), e);
        }
        return false;
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
    public ArrayList<User> getCustomersByTrainer(int trainerId) {
        ArrayList<User> customerList = new ArrayList<>();
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

            int rowCount = 0;
            while (rs.next()) {
                User user = new model.User();
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
                rowCount++;
                System.out.println("Found customer: " + user.getUserName() + " (ID: " + user.getUserId() + ", Role: " + user.getRole() + ")");
            }

            System.out.println("Total customers found: " + rowCount);

        } catch (SQLException e) {
            Logger.getLogger(ContractDAO.class.getName()).log(Level.SEVERE, "SQL Error in getCustomersByTrainer: " + e.getMessage(), e);
            System.out.println("SQL Error in getCustomersByTrainer: " + e.getMessage());
        } catch (Exception e) {
            Logger.getLogger(ContractDAO.class.getName()).log(Level.SEVERE, "Unexpected Error in getCustomersByTrainer: " + e.getMessage(), e);
            System.out.println("Unexpected Error in getCustomersByTrainer: " + e.getMessage());
        }

        System.out.println("Returning customer list with size: " + customerList.size());
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
