package dao;

import connectDB.ConnectDatabase;
import java.math.BigDecimal;
import model.Transaction;
import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TransactionDAO {

    public int addTransaction(Transaction transaction) {
        String sql = "INSERT INTO Transactions (CustomerID, Amount, Status) VALUES (?, ?, ?)";
        int transactionId = -1;

        ConnectDatabase db = ConnectDatabase.getInstance();
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = db.openConnection();
            ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, transaction.getCustomerId());
            ps.setBigDecimal(2, transaction.getAmount());
            ps.setString(3, transaction.getStatus()); 

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                transactionId = rs.getInt(1);
            }

        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(TransactionDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(TransactionDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return transactionId;
    }



    public void updateTransactionStatus(int transactionId, String status) {
        String sql = "UPDATE Transactions SET Status = ? WHERE TransactionID = ?";
        ConnectDatabase db = ConnectDatabase.getInstance();
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = db.openConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, status);
            ps.setInt(2, transactionId);
            ps.executeUpdate();
        } catch (Exception e) {
            Logger.getLogger(TransactionDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException ex) {
                Logger.getLogger(TransactionDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public Transaction getTransactionById(int id) {
        String sql = "SELECT * FROM Transactions WHERE TransactionID = ?";
        ConnectDatabase db = ConnectDatabase.getInstance();
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = db.openConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Transaction t = new Transaction();
                t.setTransactionId(rs.getInt("TransactionID"));
                t.setAmount(rs.getBigDecimal("Amount"));
                t.setCreatedTime(rs.getTimestamp("CreatedTime"));
                t.setStatus(rs.getString("Status"));
                t.setCustomerId(rs.getInt("CustomerID"));
                return t;
            }
        } catch (Exception e) {
            Logger.getLogger(TransactionDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException ex) {
                Logger.getLogger(TransactionDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
    
    public Map<String, BigDecimal> getMonthlyRevenue() {
    Map<String, BigDecimal> revenueMap = new LinkedHashMap<>();
    String sql = "SELECT CONCAT(YEAR(createdTime), '-', RIGHT('0' + CAST(MONTH(createdTime) AS VARCHAR), 2)) AS Month, "
           + "SUM(amount) AS TotalRevenue "
           + "FROM [Transaction] WHERE status = 'success' "
           + "GROUP BY YEAR(createdTime), MONTH(createdTime) ORDER BY Month ASC";


    try (Connection con = ConnectDatabase.getInstance().openConnection();
         PreparedStatement stmt = con.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {

        System.out.println("DEBUG: SQL executed");

        while (rs.next()) {
            String month = rs.getString("Month");
            BigDecimal total = rs.getBigDecimal("TotalRevenue");

            System.out.println("DEBUG: Row -> " + month + " = " + total);

            revenueMap.put(month, total);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return revenueMap;
}



}
