package dao;

import model.Program;
import connectDB.ConnectDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProgramDAO {
    public List<Program> getAllPrograms(int trainerId) {
        List<Program> programs = new ArrayList<>();
        String sql = "SELECT ProgramID, Name, Description, TrainerID, PackageID FROM Program WHERE TrainerID = ?";
        try (Connection conn = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, trainerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Program program = new Program();
                program.setProgramId(rs.getInt("ProgramID"));
                program.setName(rs.getString("Name"));
                program.setDescription(rs.getString("Description"));
                program.setTrainerId(rs.getInt("TrainerID"));
                program.setPackageId(rs.getInt("PackageID"));
                programs.add(program);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return programs;
    }

    public Program getProgramById(int programId) {
        Program program = null;
        String sql = "SELECT ProgramID, Name, Description, TrainerID, PackageID FROM Program WHERE ProgramID = ?";
        try (Connection conn = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, programId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                program = new Program();
                program.setProgramId(rs.getInt("ProgramID"));
                program.setName(rs.getString("Name"));
                program.setDescription(rs.getString("Description"));
                program.setTrainerId(rs.getInt("TrainerID"));
                program.setPackageId(rs.getInt("PackageID"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return program;
    }

    public boolean createProgram(Program program) {
        String sql = "INSERT INTO Program (Name, Description, TrainerID, PackageID) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, program.getName());
            ps.setString(2, program.getDescription());
            ps.setInt(3, program.getTrainerId());
            ps.setInt(4, program.getPackageId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateProgram(Program program) {
        String sql = "UPDATE Program SET Name = ?, Description = ?, PackageID = ? WHERE ProgramID = ? AND TrainerID = ?";
        try (Connection conn = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, program.getName());
            ps.setString(2, program.getDescription());
            ps.setInt(3, program.getPackageId());
            ps.setInt(4, program.getProgramId());
            ps.setInt(5, program.getTrainerId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteProgram(int programId) {
        String sql = "DELETE FROM Program WHERE ProgramID = ?";
        try (Connection conn = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, programId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getProgramIdByPackageId(int packageId) {
        String sql = "SELECT ProgramID FROM Program WHERE PackageID = ?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = ConnectDatabase.getInstance().openConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, packageId);
            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("ProgramID");
            }
        } catch (Exception e) {
            Logger.getLogger(ProgramDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                Logger.getLogger(ProgramDAO.class.getName()).log(Level.SEVERE, null, e);
            }
        }

        return -1; // Không tìm thấy
    }

    public List<Program> getProgramsByPackageId(int packageId) {
        List<Program> programs = new ArrayList<>();
        String sql = "SELECT ProgramID, Name, Description, TrainerID, PackageID FROM Program WHERE PackageID = ?";
        try (Connection conn = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, packageId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Program program = new Program();
                    program.setProgramId(rs.getInt("ProgramID"));
                    program.setName(rs.getString("Name"));
                    program.setDescription(rs.getString("Description"));
                    program.setTrainerId(rs.getInt("TrainerID"));
                    program.setPackageId(rs.getInt("PackageID"));
                    programs.add(program);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return programs;
    }

}