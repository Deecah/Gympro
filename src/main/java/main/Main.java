/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*<<<<<<< HEAD
=======

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*
>>>>>>> efc50787fe01a901b91052f81490e3c8eb0f3d7f
package main;

import connectDB.ConnectDatabase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Main {
    public static void main(String[] args) {
        try {
            Connection conn = ConnectDatabase.getInstance().openConnection();
<<<<<<< HEAD
            String sql = "SELECT * FROM Users";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
=======


            String sql = "SELECT * FROM Users";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();


            String sql = "SELECT * FROM Users";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

>>>>>>> efc50787fe01a901b91052f81490e3c8eb0f3d7f
            System.out.println("== Danh sách tài khoản ==");
            while (rs.next()) {
                int id = rs.getInt("Id");
                String name = rs.getString("Name");
                String email = rs.getString("Email");
                String password = rs.getString("Password");
                String role = rs.getString("Role");
                String status = rs.getString("Status");

                System.out.println("ID: " + id + " | Name: " + name + " | Email: " + email +
                        " | Role: " + role + " | Status: " + status);
            }

            rs.close();
            ps.close();
            conn.close();

        } catch (Exception e) {
            System.out.println("❌ Lỗi khi truy vấn dữ liệu từ bảng Users:");
            e.printStackTrace();
        }
    }
<<<<<<< HEAD
}
=======
}



>>>>>>> efc50787fe01a901b91052f81490e3c8eb0f3d7f
*/
