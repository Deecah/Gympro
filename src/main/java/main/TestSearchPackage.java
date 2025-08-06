/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package main;

/**
 *
 * @author Admin
 */
import controller.NotificationServlet;
import dao.PackageDAO;
import java.io.IOException;
import model.Package;

import java.util.List;

public class TestSearchPackage {

    public static void main(String[] args) throws IOException {
String hello = "hello";
NotificationServlet noti = new NotificationServlet();
noti.sendPopupNotification(hello);
    }
}
