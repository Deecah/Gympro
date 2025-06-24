package dao;

import connectDB.ConnectDatabase;
import java.math.BigDecimal;
import model.Package;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PackageDAO {
    
    public List<Package> getAllPackages() {
        String sql = "SELECT * FROM Package";
        List<Package> list = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = ConnectDatabase.getInstance().openConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Package p = new Package();
                p.setPackageID(rs.getInt("PackageID"));
                p.setName(rs.getString("PackageName"));
                p.setDescription(rs.getString("Description"));
                p.setImageUrl(rs.getString("ImageUrl"));
                p.setPrice(rs.getDouble("Price"));
                p.setDuration(rs.getInt("Duration"));
                list.add(p);
            }
        } catch (Exception e) {
            Logger.getLogger(PackageDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                Logger.getLogger(PackageDAO.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return list;
    }
    
    public List<Package> getAllPackagesByTrainer(int trainerId) {
        String sql = "SELECT * FROM Package WHERE TrainerID = ?";
        List<Package> list = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = ConnectDatabase.getInstance().openConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, trainerId);
            rs = ps.executeQuery();
            while (rs.next()) {
                Package p = new Package();
                p.setPackageID(rs.getInt("PackageID"));
                p.setName(rs.getString("PackageName"));
                p.setDescription(rs.getString("Description"));
                p.setImageUrl(rs.getString("ImageUrl"));
                p.setPrice(rs.getDouble("Price"));
                p.setDuration(rs.getInt("Duration"));
                p.setTrainerID(rs.getInt("TrainerID"));
                list.add(p);
            }
        } catch (Exception e) {
            Logger.getLogger(PackageDAO.class.getName()).log(Level.SEVERE, null, e);
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
                Logger.getLogger(PackageDAO.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return list;
    }

    public Package getPackageByIdAndTrainerId(int id, int trainerId) {
        String sql = "SELECT * FROM Package WHERE PackageID = ? AND TrainerID = ?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = ConnectDatabase.getInstance().openConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.setInt(2, trainerId);
            rs = ps.executeQuery();
            if (rs.next()) {
                Package p = new Package();
                p.setPackageID(rs.getInt("PackageID"));
                p.setName(rs.getString("PackageName"));
                p.setDescription(rs.getString("Description"));
                p.setImageUrl(rs.getString("ImageUrl"));
                p.setPrice(rs.getDouble("Price"));
                p.setDuration(rs.getInt("Duration"));
                p.setTrainerID(rs.getInt("TrainerID"));
                return p;
            }
        } catch (Exception e) {
            Logger.getLogger(PackageDAO.class.getName()).log(Level.SEVERE, null, e);
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
                Logger.getLogger(PackageDAO.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return null;
    }
    
    public Package getPackageById(int id) {
        String sql = "SELECT * FROM Package WHERE PackageID = ?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = ConnectDatabase.getInstance().openConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                Package p = new Package();
                p.setPackageID(rs.getInt("PackageID"));
                p.setName(rs.getString("PackageName"));
                p.setDescription(rs.getString("Description"));
                p.setImageUrl(rs.getString("ImageUrl"));
                p.setPrice(rs.getDouble("Price"));
                p.setDuration(rs.getInt("Duration"));
                p.setTrainerID(rs.getInt("TrainerID"));
                return p;
            }
        } catch (Exception e) {
            Logger.getLogger(PackageDAO.class.getName()).log(Level.SEVERE, null, e);
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
                Logger.getLogger(PackageDAO.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return null;
    }

    public int insertPackage(Package p, int trainerId) {
        String sql = "INSERT INTO Package (PackageName, TrainerID, Description, ImageUrl, Price, Duration) VALUES (?, ?, ?, ?, ?, ?)";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = ConnectDatabase.getInstance().openConnection();
            ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, p.getName());
            ps.setInt(2, trainerId);
            ps.setString(3, p.getDescription());
            ps.setString(4, p.getImageUrl());
            ps.setDouble(5, p.getPrice());
            ps.setInt(6, p.getDuration());
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            Logger.getLogger(PackageDAO.class.getName()).log(Level.SEVERE, "Insert package failed", e);
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
                Logger.getLogger(PackageDAO.class.getName()).log(Level.SEVERE, "Close resources failed", e);
            }
        }
        return -1;
    }

    public boolean updatePackage(Package p, int trainerId) {
        String sql = "UPDATE Package SET PackageName = ?, Description = ?, ImageUrl = ?, Price = ?, Duration = ? WHERE PackageID = ? AND TrainerID = ?";
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = ConnectDatabase.getInstance().openConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, p.getName());
            ps.setString(2, p.getDescription());
            ps.setString(3, p.getImageUrl());
            ps.setDouble(4, p.getPrice());
            ps.setInt(5, p.getDuration());
            ps.setInt(6, p.getPackageID());
            ps.setInt(7, trainerId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            Logger.getLogger(PackageDAO.class.getName()).log(Level.SEVERE, "Update package failed", e);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                Logger.getLogger(PackageDAO.class.getName()).log(Level.SEVERE, "Close resources failed", e);
            }
        }
        return false;
    }

    public boolean deletePackage(int id, int trainerId) {
        String sql = "DELETE FROM Package WHERE PackageID = ? AND TrainerID = ?";
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = ConnectDatabase.getInstance().openConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.setInt(2, trainerId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            Logger.getLogger(PackageDAO.class.getName()).log(Level.SEVERE, "Delete package failed", e);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                Logger.getLogger(PackageDAO.class.getName()).log(Level.SEVERE, "Close resources failed", e);
            }
        }
        return false;
    }

    public int getTrainerIdByPackage(int packageId) {
        String sql = "SELECT TrainerID FROM Package WHERE PackageID = ?";
        ConnectDatabase db = ConnectDatabase.getInstance();
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = db.openConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, packageId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("TrainerID");
            }
        } catch (Exception e) {
            Logger.getLogger(PackageDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException ex) {
                Logger.getLogger(PackageDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return -1;
    }

    public int getDurationByPackage(int packageId) {
        String sql = "SELECT Duration FROM Package WHERE PackageID = ?";
        ConnectDatabase db = ConnectDatabase.getInstance();
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = db.openConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, packageId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("Duration");
            }
        } catch (Exception e) {
            Logger.getLogger(PackageDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(PackageDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 0;
    }

    public BigDecimal getPriceByPackageId(int packageId) {
        String sql = "SELECT Price FROM Package WHERE PackageID = ?";
        BigDecimal price = BigDecimal.ZERO;

        ConnectDatabase db = ConnectDatabase.getInstance();
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = db.openConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, packageId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                price = rs.getBigDecimal("Price");
            }

        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(PackageDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(PackageDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return price;
    }
    public List<Package> searchByKeyword(String keyword) {
        List<Package> list = new ArrayList<>();
        String sql = "SELECT * FROM Package WHERE PackageName LIKE ? OR Description LIKE ?";

        try (Connection con = ConnectDatabase.getInstance().openConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            String searchKeyword = "%" + keyword + "%";
            ps.setString(1, searchKeyword);
            ps.setString(2, searchKeyword);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Package pkg = new Package();
                pkg.setPackageID(rs.getInt("PackageID"));
                pkg.setTrainerID(rs.getInt("TrainerID"));
                pkg.setName(rs.getString("PackageName"));
                pkg.setDescription(rs.getString("Description"));
                pkg.setImageUrl(rs.getString("ImageUrl"));
                pkg.setPrice(rs.getDouble("Price"));
                pkg.setDuration(rs.getInt("Duration"));
                list.add(pkg);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }


}