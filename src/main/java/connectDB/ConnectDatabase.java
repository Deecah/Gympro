/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package connectDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ASUS
 */
public class ConnectDatabase implements DatabaseInfor{
    private static ConnectDatabase instance;
    public Connection openConnection() throws ClassNotFoundException{
        try {
            Class.forName(driverName);
            Connection con = DriverManager.getConnection(url, user, pass);
            return con;
        } catch (SQLException e) {
            Logger.getLogger(ConnectDatabase.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }
    
    public static ConnectDatabase getInstance(){
        if(instance == null) {
            instance = new ConnectDatabase();
        }
        return instance;
    }
}
