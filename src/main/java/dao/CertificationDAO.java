package dao;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import model.Certification;
import utils.DBUtils;

public class CertificationDAO {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    public void addCertification(Certification c) throws SQLException {
        String sql = "INSERT INTO Certification (Name, Description, ExpireDate) VALUES (?, ?, ?)";
        try (Connection conn = DBUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getName());
            ps.setString(2, c.getDescription());
            ps.setTimestamp(3, Timestamp.valueOf(c.getExpireDate()));
            ps.executeUpdate();
        }
    }

    public void updateCertification(Certification c) throws SQLException {
        String sql = "UPDATE Certification SET Name=?, Description=?, ExpireDate=? WHERE CertificationID=?";
        try (Connection conn = DBUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getName());
            ps.setString(2, c.getDescription());
            ps.setTimestamp(3, Timestamp.valueOf(c.getExpireDate()));
            ps.setInt(4, c.getCertificationID());
            ps.executeUpdate();
        }
    }

    public Certification getCertificationById(int id) throws SQLException {
        String sql = "SELECT * FROM Certification WHERE CertificationID=?";
        try (Connection conn = DBUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Certification(
                        rs.getInt("CertificationID"),
                        rs.getString("Name"),
                        rs.getString("Description"),
                        rs.getTimestamp("ExpireDate").toLocalDateTime()
                    );
                }
            }
        }
        return null;
    }

    public List<Certification> getAllCertifications() throws SQLException {
        List<Certification> list = new ArrayList<>();
        String sql = "SELECT * FROM Certification";
        try (Connection conn = DBUtils.getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Certification c = new Certification(
                    rs.getInt("CertificationID"),
                    rs.getString("Name"),
                    rs.getString("Description"),
                    rs.getTimestamp("ExpireDate").toLocalDateTime()
                );
                list.add(c);
            }
        }
        return list;
    }
}
