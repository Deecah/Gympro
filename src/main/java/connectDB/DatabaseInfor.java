/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package connectDB;

/**
 *
 * @author ASUS
 */
public interface DatabaseInfor {
    public static String driverName ="com.microsoft.sqlserver.jdbc.SQLServerDriver";
    public static String  url = "jdbc:sqlserver://localhost:1433;databaseName=Gympro;encrypt=false";
    public static String user = "sa";
    public static String pass = "abc123";
}
