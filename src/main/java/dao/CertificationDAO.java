package dao;

import connectDB.ConnectDatabase;
import model.Certification;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class CertificationDAO {

    public List<Certification> getAllCertifications() {
        List<Certification> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = ConnectDatabase.getInstance().openConnection();
            String sql = "SELECT * FROM Certification";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Certification cert = new Certification();
                cert.setCertificationID(rs.getInt("CertificationID"));
                cert.setName(rs.getString("Name"));
                cert.setDescription(rs.getString("Description"));

                Date date = rs.getDate("ExpireDate");
                if (date != null) {
                    cert.setExpireDate(date.toLocalDate().atStartOfDay());
                }

                list.add(cert);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
            }
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
            }
        }

        return list;
    }
     // Thêm chứng chỉ mới
    public void addCertification(Certification cert) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO Certification (Name, Description, ExpireDate) VALUES (?, ?, ?)";
        try (Connection conn = ConnectDatabase.getInstance().openConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cert.getName());
            stmt.setString(2, cert.getDescription());
            stmt.setTimestamp(3, Timestamp.valueOf(cert.getExpireDate()));
            stmt.executeUpdate();
        }
    }
    // Cập nhật chứng chỉ
    public void updateCertification(Certification cert) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Certification SET Name = ?, Description = ?, ExpireDate = ? WHERE CertificationID = ?";
        try (Connection conn = ConnectDatabase.getInstance().openConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cert.getName());
            stmt.setString(2, cert.getDescription());
            stmt.setTimestamp(3, Timestamp.valueOf(cert.getExpireDate()));
            stmt.setInt(4, cert.getCertificationID());
stmt.executeUpdate();
        }
    }

    // Lấy 1 chứng chỉ theo ID
    public Certification getCertificationByID(int id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Certification WHERE CertificationID = ?";
        try (Connection conn = ConnectDatabase.getInstance().openConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Certification cert = new Certification();
                cert.setCertificationID(rs.getInt("CertificationID"));
                cert.setName(rs.getString("Name"));
                cert.setDescription(rs.getString("Description"));
                Timestamp ts = rs.getTimestamp("ExpireDate");
                cert.setExpireDate(ts != null ? ts.toLocalDateTime() : null);
                return cert;
            }
        }
        return null;
    }
    // Xóa chứng chỉ
    public void deleteCertification(int id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM Certification WHERE CertificationID = ?";
        try (Connection conn = ConnectDatabase.getInstance().openConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}