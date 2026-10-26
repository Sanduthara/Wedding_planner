package com.sliit.weddingplanner.repository;

import com.sliit.weddingplanner.db.DBConnection;
import com.sliit.weddingplanner.dto.PackageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// OOP: Encapsulation
// OOP: Dependency Injection
// Relationship: PackageRepository depends on DBConnection
@Repository
public class PackageRepository {

    private final DBConnection dbConnection;

    @Autowired
    public PackageRepository(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public PackageDTO save(PackageDTO packageDTO) {
        String sql = "INSERT INTO package (vendor_id, category_id, title, description, price, duration, availability) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setInt(1, packageDTO.getVendorId());
            if (packageDTO.getCategoryId() != null) ps.setInt(2, packageDTO.getCategoryId()); else ps.setNull(2, Types.INTEGER);
            ps.setString(3, packageDTO.getTitle());
            ps.setString(4, packageDTO.getDescription());
            ps.setBigDecimal(5, packageDTO.getPrice());
            ps.setString(6, packageDTO.getDuration());
            ps.setString(7, packageDTO.getAvailability() != null ? packageDTO.getAvailability() : "AVAILABLE");

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) packageDTO.setPackageId(rs.getInt(1));
            }
            return packageDTO;
        } catch (SQLException e) {
            throw new RuntimeException("Error saving package", e);
        }
    }

    public Optional<PackageDTO> findById(int id) {
        String sql = "SELECT p.*, v.name as vendor_name, " +
                     "(SELECT AVG(package_rating) FROM review r WHERE r.package_id = p.package_id) as average_rating, " +
                     "(SELECT COUNT(*) FROM review r WHERE r.package_id = p.package_id) as rating_count, " +
                     "(SELECT COUNT(*) FROM booking_package bp JOIN event_package ep ON bp.event_package_id = ep.event_package_id WHERE ep.package_id = p.package_id) as booking_count " +
                     "FROM package p JOIN vendor v ON p.vendor_id = v.vendor_id WHERE p.package_id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRowToPackage(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding package by ID", e);
        }
        return Optional.empty();
    }

    public List<PackageDTO> findAllByVendorId(int vendorId) {
        List<PackageDTO> list = new ArrayList<>();
        String sql = "SELECT p.*, v.name as vendor_name, " +
                     "(SELECT AVG(package_rating) FROM review r WHERE r.package_id = p.package_id) as average_rating, " +
                     "(SELECT COUNT(*) FROM review r WHERE r.package_id = p.package_id) as rating_count, " +
                     "(SELECT COUNT(*) FROM booking_package bp JOIN event_package ep ON bp.event_package_id = ep.event_package_id WHERE ep.package_id = p.package_id) as booking_count " +
                     "FROM package p JOIN vendor v ON p.vendor_id = v.vendor_id WHERE p.vendor_id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, vendorId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRowToPackage(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding packages by vendor", e);
        }
        return list;
    }

    public List<PackageDTO> findAll() {
        List<PackageDTO> list = new ArrayList<>();
        String sql = "SELECT p.*, v.name as vendor_name, " +
                     "(SELECT AVG(package_rating) FROM review r WHERE r.package_id = p.package_id) as average_rating, " +
                     "(SELECT COUNT(*) FROM review r WHERE r.package_id = p.package_id) as rating_count, " +
                     "(SELECT COUNT(*) FROM booking_package bp JOIN event_package ep ON bp.event_package_id = ep.event_package_id WHERE ep.package_id = p.package_id) as booking_count " +
                     "FROM package p JOIN vendor v ON p.vendor_id = v.vendor_id";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRowToPackage(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all packages", e);
        }
        return list;
    }

    public List<PackageDTO> findAllAvailablePackages() {
        List<PackageDTO> list = new ArrayList<>();
        String sql = "SELECT p.*, v.name as vendor_name, " +
                     "(SELECT AVG(package_rating) FROM review r WHERE r.package_id = p.package_id) as average_rating, " +
                     "(SELECT COUNT(*) FROM review r WHERE r.package_id = p.package_id) as rating_count, " +
                     "(SELECT COUNT(*) FROM booking_package bp JOIN event_package ep ON bp.event_package_id = ep.event_package_id WHERE ep.package_id = p.package_id) as booking_count " +
                     "FROM package p " +
                     "JOIN vendor v ON p.vendor_id = v.vendor_id " +
                     "WHERE p.availability = 'AVAILABLE' " +
                     "AND v.availability = 'AVAILABLE' " +
                     "AND v.status = 'APPROVED'";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRowToPackage(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error finding available packages", e);
        }
        return list;
    }

    public PackageDTO update(PackageDTO packageDTO) {
        String sql = "UPDATE package SET vendor_id=?, category_id=?, title=?, description=?, price=?, duration=?, availability=? WHERE package_id=?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, packageDTO.getVendorId());
            if (packageDTO.getCategoryId() != null) ps.setInt(2, packageDTO.getCategoryId()); else ps.setNull(2, Types.INTEGER);
            ps.setString(3, packageDTO.getTitle());
            ps.setString(4, packageDTO.getDescription());
            ps.setBigDecimal(5, packageDTO.getPrice());
            ps.setString(6, packageDTO.getDuration());
            ps.setString(7, packageDTO.getAvailability());
            ps.setInt(8, packageDTO.getPackageId());
            ps.executeUpdate();
            return packageDTO;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating package", e);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM package WHERE package_id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting package", e);
        }
    }

    public void updateAvailabilityByVendorId(int vendorId, String availability) {
        String sql = "UPDATE package SET availability = ? WHERE vendor_id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, availability);
            ps.setInt(2, vendorId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating packages availability for vendor", e);
        }
    }

    private PackageDTO mapRowToPackage(ResultSet rs) throws SQLException {
        PackageDTO dto = new PackageDTO();
        dto.setPackageId(rs.getInt("package_id"));
        dto.setVendorId(rs.getInt("vendor_id"));
        dto.setCategoryId(rs.getObject("category_id") != null ? rs.getInt("category_id") : null);
        dto.setTitle(rs.getString("title"));
        dto.setDescription(rs.getString("description"));
        dto.setPrice(rs.getBigDecimal("price"));
        dto.setDuration(rs.getString("duration"));
        dto.setAvailability(rs.getString("availability"));
        dto.setVendorName(rs.getString("vendor_name"));
        dto.setAverageRating(rs.getObject("average_rating") != null ? rs.getDouble("average_rating") : null);
        dto.setRatingCount(rs.getInt("rating_count"));
        dto.setBookingCount(rs.getInt("booking_count"));
        
        Timestamp created = rs.getTimestamp("created_at");
        if (created != null) dto.setCreatedAt(created.toLocalDateTime());

        return dto;
    }
}
