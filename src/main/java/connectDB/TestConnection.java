/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package connectDB;

import java.sql.Connection;
import java.sql.DriverManager;


public class TestConnection {
   public static void main(String[] args) {
        try {
      String url = "jdbc:sqlserver://LAPTOP-1JDDEEO5:1433;databaseName=Gympro;encrypt=false";
            String user = "sa";
            String password = "12345";

            Connection con = DriverManager.getConnection(url, user, password);
            System.out.println("✅ Kết nối thành công!");
            con.close();
        } catch (Exception e) {
            System.out.println("❌ Kết nối thất bại!");
            e.printStackTrace(); // Xem lỗi cụ thể
        }
    }
}

