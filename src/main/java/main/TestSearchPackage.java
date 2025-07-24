/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package main;

/**
 *
 * @author Admin
 */
import dao.PackageDAO;
import model.Package;

import java.util.List;

public class TestSearchPackage {

    public static void main(String[] args) {
        String keyword = "demo 2"; // ← Bạn có thể thay đổi từ khóa test tại đây

        PackageDAO dao = new PackageDAO();
        List<Package> packages = dao.searchByKeyword(keyword);

        System.out.println("Keyword: " + keyword);
        System.out.println("Found packages: " + packages.size());
        for (Package p : packages) {
            System.out.println(p.getPackageID() + " - " + p.getName());
        }
    }
}
