package com.sliit.weddingplanner.repository;

import com.sliit.weddingplanner.db.DBConnection;
import com.sliit.weddingplanner.dto.VendorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// OOP: Encapsulation
// OOP: Dependency Injection
// Relationship: VendorRepository depends on DBConnection
@Repository
public class VendorRepository {

    private final DBConnection dbConnection;

    @Autowired
    public VendorRepository(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public VendorDTO save(VendorDTO vendorDTO) {
        String sql = "INSERT INTO vendor (name, username, email, phone, availability, status, password) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, vendorDTO.getName());
            ps.setString(2, vendorDTO.getUsername());
            ps.setString(3, vendorDTO.getEmail());
            ps.setString(4, vendorDTO.getPhone());
            ps.setString(5, vendorDTO.getAvailability() != null ? vendorDTO.getAvailability() : "PENDING");
            ps.setString(6, vendorDTO.getStatus() != null ? vendorDTO.getStatus() : "PENDING");
            ps.setString(7, vendorDTO.getPassword());

            ps.executeUpdate();
            
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    vendorDTO.setId(rs.getInt(1));
                }
            }
            return vendorDTO;
        } catch (SQLException e) {
            throw new RuntimeException("Error saving vendor", e);
        }
    }

    public Optional<VendorDTO> findById(int id) {
        String sql = "SELECT * FROM vendor WHERE vendor_id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToVendor(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding vendor by ID", e);
        }
        return Optional.empty();
    }

    public List<VendorDTO> findAll() {
        List<VendorDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM vendor";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRowToVendor(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all vendors", e);
        }
        return list;
    }

    public void updateStatus(int vendorId, String status, int adminId) {
        String sql = "UPDATE vendor SET status = ?, approved_by = ?, approved_at = CURRENT_TIMESTAMP WHERE vendor_id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, adminId);
            ps.setInt(3, vendorId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating vendor status", e);
        }
    }

    public void updateAvailability(int vendorId, String availability) {
        String sql = "UPDATE vendor SET availability = ? WHERE vendor_id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, availability);
            ps.setInt(2, vendorId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating vendor availability", e);
        }
    }

    public boolean existsByEmail(String email) {
        String sql = "SELECT 1 FROM vendor WHERE email=?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking vendor existence", e);
        }
    }

    public boolean existsByUsername(String username) {
        String sql = "SELECT 1 FROM vendor WHERE username=?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking vendor username existence", e);
        }
    }

    public boolean existsByPhone(String phone) {
        String sql = "SELECT 1 FROM vendor WHERE phone=?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, phone);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking vendor phone existence", e);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM vendor WHERE vendor_id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting vendor", e);
        }
    }

    public VendorDTO update(VendorDTO vendorDTO) {
        String sql = "UPDATE vendor SET name=?, username=?, email=?, phone=?, availability=?, status=?, password=? WHERE vendor_id=?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, vendorDTO.getName());
            ps.setString(2, vendorDTO.getUsername());
            ps.setString(3, vendorDTO.getEmail());
            ps.setString(4, vendorDTO.getPhone());
            ps.setString(5, vendorDTO.getAvailability());
            ps.setString(6, vendorDTO.getStatus());
            ps.setString(7, vendorDTO.getPassword());
            ps.setInt(8, vendorDTO.getId());
            ps.executeUpdate();
            return vendorDTO;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating vendor", e);
        }
    }

    private VendorDTO mapRowToVendor(ResultSet rs) throws SQLException {
        VendorDTO dto = new VendorDTO();
        dto.setId(rs.getInt("vendor_id"));
        dto.setName(rs.getString("name"));
        dto.setUsername(rs.getString("username"));
        dto.setEmail(rs.getString("email"));
        dto.setPhone(rs.getString("phone"));
        dto.setAvailability(rs.getString("availability"));
        dto.setStatus(rs.getString("status"));
        dto.setApprovedBy(rs.getObject("approved_by") != null ? rs.getInt("approved_by") : null);
        dto.setApprovedAt(rs.getTimestamp("approved_at") != null ? rs.getTimestamp("approved_at").toLocalDateTime() : null);
        dto.setCreatedAt(rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null);
        return dto;
    }
}
