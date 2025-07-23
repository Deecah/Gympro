/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package main;

import dao.PackageDAO;
import model.Package;
import java.util.List;

public class TestPackage {
    public static void main(String[] args) {
        String keyword = "Gym";
        PackageDAO dao = new PackageDAO();
        List<Package> packages = dao.getAllPackages();
        
        
        

        System.out.println("Total packages: " + packages.size());
        for (Package p : packages) {
            System.out.println("ID: " + p.getPackageID());
            System.out.println("Name: " + p.getName());
            System.out.println("Description: " + p.getDescription());
            System.out.println("Price: " + p.getPrice());
            System.out.println("Duration: " + p.getDuration());
            System.out.println("Image: " + p.getImageUrl());
            System.out.println("TrainerID: " + p.getTrainerID());
            System.out.println("-------------------------");
        }
    }
}

