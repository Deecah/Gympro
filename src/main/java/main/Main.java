//
//
//package main;
//
//import connectDB.ConnectDatabase;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//
//public class Main {
//    public static void main(String[] args) {
//        try {
//            Connection conn = ConnectDatabase.getInstance().openConnection();
//
//
//            String sql = "SELECT * FROM Users";
//            PreparedStatement ps = conn.prepareStatement(sql);
//            ResultSet rs = ps.executeQuery();
//
//
////            String sql = "SELECT * FROM Users";
////            PreparedStatement ps = conn.prepareStatement(sql);
////            ResultSet rs = ps.executeQuery();
//
//            System.out.println("== Danh sách tài khoản ==");
//            while (rs.next()) {
//                int id = rs.getInt("Id");
//                String name = rs.getString("Name");
//                String email = rs.getString("Email");
//                String password = rs.getString("Password");
//                String role = rs.getString("Role");
//                String status = rs.getString("Status");
//
//                System.out.println("ID: " + id + " | Name: " + name + " | Email: " + email +
//                        " | Role: " + role + " | Status: " + status);
//            }
//
//            rs.close();
//            ps.close();
//            conn.close();
//
//        } catch (Exception e) {
//            System.out.println("❌ Lỗi khi truy vấn dữ liệu từ bảng Users:");
//            e.printStackTrace();
//        }
//    }
//}
//
//
//
