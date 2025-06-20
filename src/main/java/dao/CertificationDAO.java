package dao;

import connectDB.ConnectDatabase;
import model.Certification;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CertificationDAO {

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

    // Lấy tất cả chứng chỉ
    public List<Certification> getAllCertifications() throws SQLException, ClassNotFoundException {
        List<Certification> list = new ArrayList<>();
        String sql = "SELECT * FROM Certification ORDER BY ExpireDate DESC";
        try (Connection conn = ConnectDatabase.getInstance().openConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Certification cert = new Certification();
                cert.setCertificationID(rs.getInt("CertificationID"));
                cert.setName(rs.getString("Name"));
                cert.setDescription(rs.getString("Description"));
                Timestamp ts = rs.getTimestamp("ExpireDate");
                cert.setExpireDate(ts != null ? ts.toLocalDateTime() : null);
                list.add(cert);
            }
        }
        return list;
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
