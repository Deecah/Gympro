/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import connectDB.ConnectDatabase;
import model.Customer;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomerDAO {

    public Customer getProfile(int userId) {
        String userSQL = "SELECT Name, Gender, Email, Phone, Address, AvatarUrl FROM Users WHERE Id = ?";
        String customerSQL = "SELECT Weight, Height, Goal, MedicalConditions FROM Customer WHERE Id = ?";

        ConnectDatabase db = ConnectDatabase.getInstance();
        Connection con = null;
        PreparedStatement userStmt = null;
        PreparedStatement customerStmt = null;
        ResultSet userRs = null;
        ResultSet customerRs = null;

        try {
            con = db.openConnection();
            userStmt = con.prepareStatement(userSQL);
            userStmt.setInt(1, userId);
            userRs = userStmt.executeQuery();
            if (!userRs.next()) {
                return null;
            }
            Customer customer = new Customer();
            customer.setUserId(userId);
            customer.setUserName(userRs.getString("Name"));
            customer.setGender(userRs.getString("Gender"));
            customer.setEmail(userRs.getString("Email"));
            customer.setPhone(userRs.getString("Phone"));
            customer.setAddress(userRs.getString("Address"));
            customer.setAvatarUrl(userRs.getString("AvatarUrl"));

            customerStmt = con.prepareStatement(customerSQL);
            customerStmt.setInt(1, userId);
            customerRs = customerStmt.executeQuery();

            if (customerRs.next()) {
                customer.setWeight(customerRs.getDouble("Weight"));
                customer.setHeight(customerRs.getDouble("Height"));
                customer.setGoal(customerRs.getString("Goal"));
                customer.setMedicalConditions(customerRs.getString("MedicalConditions"));
            }

            return customer;

        } catch (Exception e) {
            Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, "Get profile failed", e);
            return null;

        } finally {
            try {
                if (userRs != null) {
                    userRs.close();
                }
                if (customerRs != null) {
                    customerRs.close();
                }
                if (userStmt != null) {
                    userStmt.close();
                }
                if (customerStmt != null) {
                    customerStmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, "Close resources failed", ex);
            }
        }
    }

    public boolean editProfile(Customer customer) {

        String updateUserSQL = "UPDATE Users SET Name = ?, Gender = ?, Phone = ?, Address = ?, AvatarUrl = ? WHERE Id = ?";
        String updateCustomerSQL = "UPDATE Customer SET Weight = ?, Height = ?, Goal = ?, MedicalConditions = ? WHERE Id = ?";
        ConnectDatabase db = ConnectDatabase.getInstance();
        Connection con = null;
        PreparedStatement userStmt = null;
        PreparedStatement customerStmt = null;

        try {
            con = db.openConnection();
            con.setAutoCommit(false); // đảm bảo tính toàn vẹn

            // Update bảng Users
            userStmt = con.prepareStatement(updateUserSQL);
            userStmt.setString(1, customer.getUserName());
            userStmt.setString(2, customer.getGender());
            userStmt.setString(3, customer.getPhone());
            userStmt.setString(4, customer.getAddress());
            userStmt.setString(5, customer.getAvatarUrl());
            userStmt.setInt(6, customer.getUserId());
            userStmt.executeUpdate();

            // Update bảng Customer
            customerStmt = con.prepareStatement(updateCustomerSQL);
            customerStmt.setDouble(1, customer.getWeight());
            customerStmt.setDouble(2, customer.getHeight());
            customerStmt.setString(3, customer.getGoal());
            customerStmt.setString(4, customer.getMedicalConditions());
            customerStmt.setInt(5, customer.getUserId());
            customerStmt.executeUpdate();

            con.commit();
            return true;

        } catch (Exception e) {
            try {
                if (con != null) {
                    con.rollback();
                }
            } catch (SQLException ex) {
                Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, "Rollback failed", ex);
            }
            Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, "Edit profile failed", e);
            return false;

        } finally {
            try {
                if (userStmt != null) {
                    userStmt.close();
                }
                if (customerStmt != null) {
                    customerStmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, "Close resources failed", ex);
            }
        }
    }

}