package com.sliit.weddingplanner.repository;

import com.sliit.weddingplanner.db.DBConnection;
import com.sliit.weddingplanner.dto.AdminDTO;
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
// Relationship: AdminRepository depends on DBConnection
@Repository
public class AdminRepository {

    private final DBConnection dbConnection;

    @Autowired
    public AdminRepository(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public AdminDTO save(AdminDTO adminDTO) {
        String sql = "INSERT INTO admin (name, username, email, password, role) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, adminDTO.getName());
            ps.setString(2, adminDTO.getUsername());
            ps.setString(3, adminDTO.getEmail());
            ps.setString(4, adminDTO.getPassword());
            ps.setString(5, adminDTO.getRole());

            ps.executeUpdate();
            
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    adminDTO.setId(rs.getInt(1));
                }
            }
            return adminDTO;
        } catch (SQLException e) {
            throw new RuntimeException("Error saving admin: " + e.getMessage(), e);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM admin WHERE admin_id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting admin", e);
        }
    }

    public Optional<AdminDTO> findById(int id) {
        String sql = "SELECT * FROM admin WHERE admin_id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToAdmin(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding admin by ID", e);
        }
        return Optional.empty();
    }

    public List<AdminDTO> findAll() {
        List<AdminDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM admin";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                list.add(mapRowToAdmin(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all admins", e);
        }
        return list;
    }

    public AdminDTO update(AdminDTO adminDTO) {
        String sql = "UPDATE admin SET name=?, username=?, email=?, role=? WHERE admin_id=?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             
            ps.setString(1, adminDTO.getName());
            ps.setString(2, adminDTO.getUsername());
            ps.setString(3, adminDTO.getEmail());
            ps.setString(4, adminDTO.getRole());
            ps.setInt(5, adminDTO.getId());

            ps.executeUpdate();
            return adminDTO;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating admin", e);
        }
    }

    public boolean existsByUsernameOrEmail(String username, String email) {
        String sql = "SELECT 1 FROM admin WHERE username=? OR email=?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             
            ps.setString(1, username);
            ps.setString(2, email);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking admin existence", e);
        }
    }

    private AdminDTO mapRowToAdmin(ResultSet rs) throws SQLException {
        AdminDTO admin = new AdminDTO();
        admin.setId(rs.getInt("admin_id"));
        admin.setName(rs.getString("name"));
        admin.setUsername(rs.getString("username"));
        admin.setEmail(rs.getString("email"));
        admin.setRole(rs.getString("role"));
        admin.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return admin;
    }
}
