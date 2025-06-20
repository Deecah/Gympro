package main;

import dao.CertificationDAO;
import model.Certification;

import java.util.List;

public class TestCertificationDAO {
    public static void main(String[] args) {
        CertificationDAO dao = new CertificationDAO();
        List<Certification> certList = dao.getAllCertifications();

        if (certList.isEmpty()) {
            System.out.println("No certifications found in the database.");
        } else {
            for (Certification cert : certList) {
                System.out.println(cert);
            }
        }
    }
}
