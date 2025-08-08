package dao;

import connectDB.ConnectDatabase;
import model.Program;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProgramDAO {

    /**
     * Lấy danh sách chương trình theo TrainerID
     * @param trainerId ID của huấn luyện viên
     * @return Danh sách Program
     */
    public List<Program> getAllPrograms(int trainerId) {
        List<Program> programs = new ArrayList<>();
        String sql = "SELECT ProgramID, Name, Description, TrainerID, PackageID FROM Program WHERE TrainerID = ?";
        try (Connection conn = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, trainerId);
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
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return programs;
    }

    /**
     * Lấy chương trình theo ProgramID
     * @param programId ID của chương trình
     * @return Đối tượng Program hoặc null nếu không tìm thấy
     */
    public Program getProgramById(int programId) {
        String sql = "SELECT ProgramID, Name, Description, TrainerID, PackageID FROM Program WHERE ProgramID = ?";
        try (Connection conn = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, programId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Program program = new Program();
                    program.setProgramId(rs.getInt("ProgramID"));
                    program.setName(rs.getString("Name"));
                    program.setDescription(rs.getString("Description"));
                    program.setTrainerId(rs.getInt("TrainerID"));
                    program.setPackageId(rs.getInt("PackageID"));
                    return program;
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Tạo một chương trình mới
     * @param program Đối tượng Program
     * @return true nếu tạo thành công, false nếu thất bại
     */
    public boolean createProgram(Program program) {
        String sql = "INSERT INTO Program (Name, Description, TrainerID, PackageID) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, program.getName());
            ps.setString(2, program.getDescription());
            ps.setInt(3, program.getTrainerId());
            ps.setInt(4, program.getPackageId());
            return ps.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Cập nhật chương trình
     * @param program Đối tượng Program
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
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
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Xóa chương trình
     * @param programId ID của chương trình
     * @return true nếu xóa thành công, false nếu thất bại
     */
    public boolean deleteProgram(int programId) {
        String sql = "DELETE FROM Program WHERE ProgramID = ?";
        try (Connection conn = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, programId);
            return ps.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Lấy ProgramID theo PackageID
     * @param packageId ID của gói
     * @return ProgramID hoặc -1 nếu không tìm thấy
     */
    public int getProgramIdByPackageId(int packageId) {
        String sql = "SELECT ProgramID FROM Program WHERE PackageID = ?";
        try (Connection conn = ConnectDatabase.getInstance().openConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, packageId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("ProgramID");
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Lấy danh sách chương trình theo PackageID
     * @param packageId ID của gói
     * @return Danh sách Program
     */
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
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return programs;
    }
}