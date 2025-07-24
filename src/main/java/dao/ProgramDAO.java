/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import connectDB.ConnectDatabase;
import model.Program;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProgramDAO {

    public List<Program> getAllProgramsByTrainer(int trainerId) {
        String sql = "SELECT * FROM Program WHERE TrainerID = ?";
        ConnectDatabase db = ConnectDatabase.getInstance();
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Program> list = new ArrayList<>();

        try {
            con = db.openConnection();
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, trainerId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Program p = new Program();
                p.setProgramId(rs.getInt("ProgramID"));
                p.setName(rs.getString("Name"));
                p.setDescription(rs.getString("Description"));
                p.setTrainerId(rs.getInt("TrainerID"));

                Object pkgObj = rs.getObject("PackageID");
                if (pkgObj != null) {
                    p.setPackageId((Integer) pkgObj);
                } else {
                    p.setPackageId(null);
                }
                list.add(p);
            }

        } catch (ClassNotFoundException | SQLException e) {
            Logger.getLogger(ProgramDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                Logger.getLogger(ProgramDAO.class.getName()).log(Level.SEVERE, null, e);
            }
        }

        return list;
    }

    public Program getProgramById(int programId, int trainerId) {
        String sql = "SELECT * FROM Program WHERE ProgramID = ? AND TrainerID = ?";
        ConnectDatabase db = ConnectDatabase.getInstance();
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            con = db.openConnection();
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, programId);
            stmt.setInt(2, trainerId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                Program program = new Program();
                program.setProgramId(rs.getInt("ProgramID"));
                program.setName(rs.getString("Name"));
                program.setDescription(rs.getString("Description"));
                program.setTrainerId(rs.getInt("TrainerID"));
                program.setPackageId(rs.getInt("PackageID"));
                return program;
            }

        } catch (ClassNotFoundException | SQLException e) {
            Logger.getLogger(ProgramDAO.class.getName()).log(Level.SEVERE, "Get program by ID failed", e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                Logger.getLogger(ProgramDAO.class.getName()).log(Level.SEVERE, "Close resources failed", e);
            }
        }

        return null;
    }

    public int addProgram(Program p, int trainerId) {
        String sql = "INSERT INTO Program (Name, Description, TrainerID, PackageID) VALUES (?, ?, ?, ?)";
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            con = ConnectDatabase.getInstance().openConnection();
            stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, p.getName());
            stmt.setString(2, p.getDescription());
            stmt.setInt(3, trainerId);
            if (p.getPackageId() > 0) {
                stmt.setInt(4, p.getPackageId());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }
            stmt.executeUpdate();
            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            Logger.getLogger(ProgramDAO.class.getName()).log(Level.SEVERE, "Add program failed", e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                Logger.getLogger(ProgramDAO.class.getName()).log(Level.SEVERE, "Close resources failed", e);
            }
        }
        return -1;
    }

    public boolean updateProgram(Program p, int trainerId) {
        String sql = "UPDATE Program SET Name = ?, Description = ?, PackageID = ? WHERE ProgramID = ? AND TrainerID = ?";
        Connection con = null;
        PreparedStatement stmt = null;
        try {
            con = ConnectDatabase.getInstance().openConnection();
            stmt = con.prepareStatement(sql);
            stmt.setString(1, p.getName());
            stmt.setString(2, p.getDescription());
            if (p.getPackageId() > 0) {
                stmt.setInt(3, p.getPackageId());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }
            stmt.setInt(4, p.getProgramId());
            stmt.setInt(5, trainerId);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            Logger.getLogger(ProgramDAO.class.getName()).log(Level.SEVERE, "Update program failed", e);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                Logger.getLogger(ProgramDAO.class.getName()).log(Level.SEVERE, "Close resources failed", e);
            }
        }
        return false;
    }

    public boolean deleteProgram(int id) {
        String sql = "DELETE FROM Program WHERE ProgramID = ?";
        Connection con = null;
        PreparedStatement stmt = null;
        try {
            con = ConnectDatabase.getInstance().openConnection();
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            Logger.getLogger(ProgramDAO.class.getName()).log(Level.SEVERE, "Delete program failed", e);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                Logger.getLogger(ProgramDAO.class.getName()).log(Level.SEVERE, "Close resources failed", e);
            }
        }
        return false;
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

}
