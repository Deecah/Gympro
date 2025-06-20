/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import connectDB.ConnectDatabase;
import model.Package;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PackageDAO {
    public List<Package> searchByKeyword(String keyword) {
        List<Package> list = new ArrayList<>();
        String sql = "SELECT * FROM Package WHERE PackageName LIKE ? OR Description LIKE ?";

        try (Connection con = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Package p = new Package();
                p.setPackageID(rs.getInt("PackageID"));
                p.setTrainerID(rs.getInt("Trainer_ID"));
                p.setName(rs.getString("PackageName"));
                p.setDescription(rs.getString("Description"));
                p.setPrice(rs.getDouble("Price"));
                p.setDuration(rs.getInt("Duration"));
                list.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}

